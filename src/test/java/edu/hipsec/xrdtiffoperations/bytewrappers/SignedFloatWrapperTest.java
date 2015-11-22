package edu.hipsec.xrdtiffoperations.bytewrappers;

import edu.hipsec.xrdtiffoperations.bytewrappers.base.ByteWrapperTest;
import org.junit.Assert;
import org.junit.Before;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SignedFloatWrapperTest extends ByteWrapperTest<SignedFloatWrapper> {

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        Float value = (float) 1.5;
        int numBytes = Float.BYTES;
        ByteBuffer buffer = ByteBuffer.allocate(numBytes);
        buffer.putFloat(value);
        this.wrapper = new SignedFloatWrapper(ByteOrder.BIG_ENDIAN);
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
        float value = this.wrapper.get();
        Assert.assertEquals((float) 1.5, value, 0.0);
    }

}
