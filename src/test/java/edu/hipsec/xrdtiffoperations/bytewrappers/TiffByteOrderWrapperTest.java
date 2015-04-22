package edu.hipsec.xrdtiffoperations.bytewrappers;

import edu.hipsec.xrdtiffoperations.bytewrappers.base.ByteWrapperTest;
import org.junit.Assert;
import org.junit.Before;
import java.nio.ByteOrder;

public class TiffByteOrderWrapperTest extends ByteWrapperTest<TiffByteOrderWrapper> {

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        String value = "II";
        this.wrapper = new TiffByteOrderWrapper();
        this.bytes = value.getBytes();
    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Override
    public void constructor_checkArraySize_expectedByteLengthForType() {
        Assert.assertEquals(2, this.wrapper.getDataBytes().length);
    }

    @Override
    public void get_valueOfTypeConverted_returnInput() {
        this.wrapper.extractFromSourceArray(this.bytes, 0);
        ByteOrder value = this.wrapper.get();
        Assert.assertEquals(ByteOrder.LITTLE_ENDIAN, value);
    }

}
