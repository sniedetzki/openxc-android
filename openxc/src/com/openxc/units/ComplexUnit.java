package com.openxc.units;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.openxc.measurements.Measurement;

/**
 * A complex unit consisting of multiple arbitrary parts.
 */
public class ComplexUnit extends Unit {
    private Map<String, Measurement> mValue;


    public ComplexUnit(Map<String, Measurement> value) {
        mValue = value;
    }

    public ComplexUnit() {
        this(new HashMap<String, Measurement>());
    }

    public Object getSerializedValue() {
        // TODO need to render out to serialized JSON?
        return mValue;
    }

    public void put(String name, Measurement value) {
        mValue.put(name, value);
    }

    public Measurement get(String name) {
        return mValue.get(name);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("value", mValue)
            .toString();
    }

}
