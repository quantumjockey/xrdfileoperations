package edu.hipsec.xrdtiffoperations.bytewrappers;

import edu.hipsec.xrdtiffoperations.bytewrappers.base.ByteWrapperTest;
import org.junit.Assert;
import org.junit.Before;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SignedIntWrapperTest extends ByteWrapperTest<SignedIntWrapper> {

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

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Override
    public void constructor_checkArraySize_expectedByteLengthForType() {
        Assert.assertEquals(4, this.wrapper.getDataBytes().length);
    }

    @Override
    public void get_valueOfTypeConverted_returnInput() {
        this.wrapper.extractFromSourceArray(this.bytes, 0);
        int value = this.wrapper.get();
        Assert.assertEquals(6, value);
    }

}
