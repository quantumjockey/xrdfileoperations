package edu.hipsec.xrdtiffoperations.bytewrappers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SignedFloatWrapperTest {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    byte[] bytes;
    SignedFloatWrapper wrapper;

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        Float value = (float)1.5;
        int numBytes = Float.BYTES;
        ByteBuffer buffer = ByteBuffer.allocate(numBytes);
        buffer.putFloat(value);
        this.wrapper = new SignedFloatWrapper(ByteOrder.BIG_ENDIAN);
        this.bytes = buffer.array();
    }

    @After
    public void tearDown() throws Exception {

    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void get_floatWithinBoundsConverted_returnInput() {
        this.wrapper.extractFromSourceArray(this.bytes, 0);
        float value = this.wrapper.get();
        Assert.assertEquals((float)1.5, value, 0.0);
    }

}

