package com.openxc.remote;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.common.base.Objects;
import com.openxc.measurements.UnrecognizedMeasurementTypeException;
import com.openxc.measurements.serializers.JsonSerializer;

/**
 * An untyped measurement used only for the AIDL VehicleService interface.
 *
 * This abstract base class is intended to be the parent of numerical, state and
 * boolean measurements. The architecture ended up using only numerical
 * measurements, with other types being coerced to doubles.
 *
 * A raw measurement has a value, which can be any type valid in JSON. Most
 * measurements have only a primitive value, but some have objects as values
 * which have their own, arbitrary properties (e.g. button events, door status,
 * GPS location). The value may be null, for cases where a measurement needs to
 * be returned but there is no valid value for it.
 *
 * This class implements the Parcelable interface, so it can be used directly as
 * a return value or function parameter in an AIDL interface.
 *
 * @see com.openxc.measurements.BaseMeasurement
 */
public class RawMeasurement implements Parcelable {
    private static final String TAG = "RawMeasurement";

    private String mCachedSerialization;
    private double mTimestamp;
    private String mName;
    private Object mValue;

    public RawMeasurement(String name, Object value) {
        this();
        mName = name;
        mValue = value;
    }

    public RawMeasurement(String name, Object value, double timestamp) {
        this(name, value);
        mTimestamp = timestamp;
        timestamp();
    }

    public RawMeasurement(String serialized)
            throws UnrecognizedMeasurementTypeException {
        deserialize(serialized, this);
        timestamp();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getName());
        out.writeDouble(getTimestamp());
        out.writeValue(getValue());
    }

    public void readFromParcel(Parcel in) {
        mName = in.readString();
        mTimestamp = in.readDouble();
        mValue = in.readValue(null);
    }

    public static final Parcelable.Creator<RawMeasurement> CREATOR =
            new Parcelable.Creator<RawMeasurement>() {
        public RawMeasurement createFromParcel(Parcel in) {
            try {
                return new RawMeasurement(in);
            } catch(UnrecognizedMeasurementTypeException e) {
                return new RawMeasurement();
            }
        }

        public RawMeasurement[] newArray(int size) {
            return new RawMeasurement[size];
        }
    };

    public String serialize() {
        return serialize(false);
    }

    public String serialize(boolean reserialize) {
        if(reserialize || mCachedSerialization == null) {
            Double timestamp = isTimestamped() ? getTimestamp() : null;
            mCachedSerialization = JsonSerializer.serialize(getName(),
                    getValue(), timestamp);
        }
        return mCachedSerialization;
    }

    public String getName() {
        return mName;
    }

    public Object getValue() {
        return mValue;
    }

    /**
     * @return true if the measurement has a valid timestamp.
     */
    public boolean isTimestamped() {
        return getTimestamp() != null && !Double.isNaN(getTimestamp())
            && getTimestamp() != 0;
    }

    public Double getTimestamp() {
        return mTimestamp;
    }

    /**
     * Make the measurement's timestamp invalid so it won't end up in the
     * serialized version.
     */
    public void untimestamp() {
    	mTimestamp = Double.NaN;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("value", getValue())
            .toString();
    }

    private static void deserialize(String measurementString,
            RawMeasurement measurement)
            throws UnrecognizedMeasurementTypeException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser;
        try {
            parser = jsonFactory.createParser(measurementString);
        } catch(IOException e) {
            String message = "Couldn't decode JSON from: " + measurementString;
            Log.w(TAG, message, e);
            throw new UnrecognizedMeasurementTypeException(message, e);
        }

        try {
            parser.nextToken();
            while(parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                JsonToken token = parser.nextToken();
                if(JsonSerializer.NAME_FIELD.equals(field)) {
                    measurement.mName = parser.getText();
                } else if(JsonSerializer.VALUE_FIELD.equals(field)) {
                    measurement.mValue = parseUnknownType(parser, token);
                } else if(JsonSerializer.TIMESTAMP_FIELD.equals(field)) {
                    measurement.mTimestamp =
                        parser.getNumberValue().doubleValue();
                }
            }

            if(measurement.mName == null) {
                throw new UnrecognizedMeasurementTypeException(
                        "Missing name in: " + measurementString);
            }
            if(measurement.mValue == null) {
                throw new UnrecognizedMeasurementTypeException(
                        "Missing value in: " + measurementString);
            }
        } catch(IOException e) {
            String message = "JSON message didn't have the expected format: "
                    + measurementString;
            Log.w(TAG, message, e);
            throw new UnrecognizedMeasurementTypeException(message, e);
        }
        measurement.mCachedSerialization = measurementString;
    }

    private static Object parseUnknownType(JsonParser parser, JsonToken token) {
        Object value = null;
        try {
            if(token == JsonToken.START_OBJECT) {
                Map<String, Object> valueObject = new HashMap<String, Object>();
                while(parser.nextToken() != JsonToken.END_OBJECT) {
                    String field = parser.getCurrentName();
                    token = parser.nextToken();
                    Object subValue = parseUnknownType(parser, token);
                    valueObject.put(field, subValue);
                }
                value = valueObject;
            } else {
                try {
                    value = parser.getNumberValue();
                } catch(JsonParseException e) {
                    try {
                        value = parser.getBooleanValue();
                    } catch(JsonParseException e2) {
                        try {
                            value = parser.getText();
                        } catch(JsonParseException e3) {
                        } catch(IOException e4) {
                        }
                    } catch(IOException e5) {
                    }
                }
            }
        } catch(IOException e) {
        }
        return value;
    }

    private RawMeasurement(Parcel in)
            throws UnrecognizedMeasurementTypeException {
        this();
        readFromParcel(in);
        timestamp();
    }

    private RawMeasurement() {
        timestamp();
    }

    private void timestamp() {
        if(!isTimestamped()) {
            mTimestamp = System.currentTimeMillis() / 1000.0;
        }
    }
}
