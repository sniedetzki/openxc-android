package com.openxc.measurements;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import junit.framework.TestCase;

import com.openxc.units.State;

public class VehicleButtonEventTest extends TestCase {
    VehicleButtonEvent measurement;

    @Override
    public void setUp() {
        measurement = new VehicleButtonEvent(
                new State<VehicleButtonEvent.ButtonId>(
                    VehicleButtonEvent.ButtonId.OK));
    }

    public void testGet() {
        assertThat(measurement.getValue().enumValue(), equalTo(
                    VehicleButtonEvent.ButtonId.OK));
        // TODO test new style value map instead of old event
    }

    public void testHasNoRange() {
        assertFalse(measurement.hasRange());
    }
}
