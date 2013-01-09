package com.openxc.measurements;

import java.util.Map;
import java.util.HashMap;
import junit.framework.TestCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.openxc.units.Degree;

public class LocationTest extends TestCase {
    Location measurement;

    @Override
    public void setUp() {
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("latitude", Double.valueOf(42.0));
        value.put("longitude", Double.valueOf(100.0));
        measurement = new Location(value);
    }

    public void testGet() {
        assertThat(measurement.getLatitude().getValue().doubleValue(), equalTo(42.0));
        assertThat(measurement.getLongitude().getValue().doubleValue(), equalTo(100.0));
    }

    public void testHasRange() {
        assertTrue(measurement.getLongitude().hasRange());
        assertTrue(measurement.getLatitude().hasRange());
        assertFalse(measurement.hasRange());
    }
}
