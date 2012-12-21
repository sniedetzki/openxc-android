package com.openxc.it.sinks;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.openxc.sinks.ContextualVehicleDataSink;

public class ContextualVehicleDataSinkTest extends AndroidTestCase {
    ContextualVehicleDataSink sink;

    @SmallTest
    public void testConstructWithContext() {
        sink = new ContextualVehicleDataSink(getContext());
        // getContext is protected so we can't really test that it works
    }
}
