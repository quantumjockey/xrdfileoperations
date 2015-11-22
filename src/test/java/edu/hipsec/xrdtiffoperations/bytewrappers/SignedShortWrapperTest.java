package edu.hipsec.xrdtiffoperations.bytewrappers;

import edu.hipsec.xrdtiffoperations.bytewrappers.base.ByteWrapperTest;
import org.junit.Assert;
import org.junit.Before;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SignedShortWrapperTest extends ByteWrapperTest<SignedShortWrapper> {

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        short value = (short) 4;
        int numBytes = Short.BYTES;
        ByteBuffer buffer = ByteBuffer.allocate(numBytes);
        buffer.putShort(value);
        this.wrapper = new SignedShortWrapper(ByteOrder.BIG_ENDIAN);
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
        short value = this.wrapper.get();
        Assert.assertEquals((short) 4, value);
    }

}
