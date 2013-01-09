package com.openxc.units;

import java.util.List;

import com.google.common.base.Objects;

/**
 * A unit consisting of multiple values of the same unit type.
 */
public class MultiUnit<T extends Unit> extends Unit {
    private List<T> mValue;

    public MultiUnit(List<T> value) {
        mValue = value;
    }

    public Object getSerializedValue() {
        // TODO need to render out to serialized JSON?
        return mValue;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("value", mValue)
            .toString();
    }

}
