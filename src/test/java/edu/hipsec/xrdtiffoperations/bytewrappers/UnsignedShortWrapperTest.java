package edu.hipsec.xrdtiffoperations.bytewrappers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UnsignedShortWrapperTest {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    byte[] bytes;
    UnsignedShortWrapper wrapper;

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        char value = 'a';
        int numBytes = Character.BYTES;
        ByteBuffer buffer = ByteBuffer.allocate(numBytes);
        buffer.putChar(value);
        this.wrapper = new UnsignedShortWrapper(ByteOrder.BIG_ENDIAN);
        this.bytes = buffer.array();
    }

    @After
    public void tearDown() throws Exception {

    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void get_characterConverted_returnInput() {
        System.arraycopy(this.bytes, 0, this.wrapper.getDataBytes(), 0, this.bytes.length);
        char value = this.wrapper.get();
        Assert.assertEquals('a', value);
    }

}
