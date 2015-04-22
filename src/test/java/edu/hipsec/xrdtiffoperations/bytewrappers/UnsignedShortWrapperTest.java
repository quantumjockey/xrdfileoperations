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
    public void constructor_checkArraySize_expectedByteLengthForType() {
        Assert.assertEquals(2, this.wrapper.getDataBytes().length);
    }

    @Test
    public void get_characterConverted_returnInput() {
        this.wrapper.extractFromSourceArray(this.bytes, 0);
        char value = this.wrapper.get();
        Assert.assertEquals('a', value);
    }

}
