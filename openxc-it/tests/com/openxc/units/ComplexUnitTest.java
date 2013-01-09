package com.openxc.units;

import junit.framework.TestCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.openxc.measurements.Latitude;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.measurements.TransmissionGearPosition;

public class ComplexUnitTest extends TestCase {
    ComplexUnit unit;

    private enum TestState {
        ON,
        OFF,
        PEANUT_BUTTER;
    }

    @Override
    public void setUp() {
        unit = new ComplexUnit();
        unit.put("first", new VehicleSpeed(1));
        unit.put("second", new Latitude(20));
        unit.put("third", new TransmissionGearPosition(
                    TransmissionGearPosition.GearPosition.FIRST));
    }


    public void testGet() {
        assertNotNull(unit.get("first"));
        assertNotNull(unit.get("second"));
        assertNotNull(unit.get("third"));
        assertThat(((VehicleSpeed)unit.get("first")).getValue().doubleValue(), equalTo(1.0));
        assertThat(((Latitude)unit.get("second")).getValue().doubleValue(), equalTo(20.0));
        assertThat(((TransmissionGearPosition)unit.get("third")).getValue().enumValue(),
                equalTo(TransmissionGearPosition.GearPosition.FIRST));
    }
}
