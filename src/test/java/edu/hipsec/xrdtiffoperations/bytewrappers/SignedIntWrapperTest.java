package edu.hipsec.xrdtiffoperations.bytewrappers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SignedIntWrapperTest {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    byte[] bytes;
    SignedIntWrapper wrapper;

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        Integer value = 6;
        int numBytes = Integer.BYTES;
        ByteBuffer buffer = ByteBuffer.allocate(numBytes);
        buffer.putInt(value);
        this.wrapper = new SignedIntWrapper(ByteOrder.BIG_ENDIAN);
        this.bytes = buffer.array();
    }

    @After
    public void tearDown() throws Exception {

    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void get_integerWithinBoundsConverted_returnInput() {
        this.wrapper.extractFromSourceArray(this.bytes, 0);
        int value = this.wrapper.get();
        Assert.assertEquals(6, value);
    }

}
