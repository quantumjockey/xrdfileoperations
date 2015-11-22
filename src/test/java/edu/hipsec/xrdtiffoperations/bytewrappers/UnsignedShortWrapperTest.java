package edu.hipsec.xrdtiffoperations.bytewrappers;

import edu.hipsec.xrdtiffoperations.bytewrappers.base.ByteWrapperTest;
import org.junit.Assert;
import org.junit.Before;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UnsignedShortWrapperTest extends ByteWrapperTest<UnsignedShortWrapper> {

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

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Override
    public void constructor_checkArraySize_expectedByteLengthForType() {
        Assert.assertEquals(2, this.wrapper.getDataBytes().length);
    }

    @Override
    public void get_valueOfTypeConverted_returnInput() {
        this.wrapper.extractFromSourceArray(this.bytes, 0);
        char value = this.wrapper.get();
        Assert.assertEquals('a', value);
    }

}
