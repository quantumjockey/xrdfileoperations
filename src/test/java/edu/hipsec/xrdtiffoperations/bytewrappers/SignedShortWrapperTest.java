package edu.hipsec.xrdtiffoperations.bytewrappers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SignedShortWrapperTest {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    byte[] bytes;
    SignedShortWrapper wrapper;

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        short value = (short)4;
        int numBytes = Short.BYTES;
        ByteBuffer buffer = ByteBuffer.allocate(numBytes);
        buffer.putShort(value);
        this.wrapper = new SignedShortWrapper(ByteOrder.BIG_ENDIAN);
        this.bytes = buffer.array();
    }

    @After
    public void tearDown() throws Exception {

    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void get_shortWithinBoundsConverted_returnInput() {
        System.arraycopy(this.bytes, 0, this.wrapper.getDataBytes(), 0, this.bytes.length);
        short value = this.wrapper.get();
        Assert.assertEquals((short)4, value);
    }
    
}
