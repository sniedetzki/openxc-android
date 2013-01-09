package com.openxc.measurements;

import java.util.Map;

import com.openxc.units.ComplexUnit;

/**
 * Location is a complex measurement that contains a GPS location, which
 * includes latitude and longitude.
 */
public class Location extends BaseMeasurement<ComplexUnit> {
    public final static String ID = "location";

    public Location(ComplexUnit value) {
        super(value, null);
    }

    public Location(Map<String, Object> value) {
        this(new ComplexUnit());
        getValue().put("latitude", new Latitude((Number)value.get("latitude")));
        getValue().put("longtidue", new Longitude((Number)value.get("longitude")));
    }

    public Latitude getLatitude() {
        return (Latitude) getValue().get("latitude");
    }

    public Longitude getLongitude() {
        return (Longitude) getValue().get("longitude");
    }

    @Override
    public String getGenericName() {
        return ID;
    }
}
