package edu.hipsec.xrdtiffoperations.bytewrappers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.nio.ByteOrder;

public class TiffByteOrderWrapperTest {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    byte[] bytes;
    TiffByteOrderWrapper wrapper;

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        String value = "II";
        this.wrapper = new TiffByteOrderWrapper();
        this.bytes = value.getBytes();
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
        ByteOrder value = this.wrapper.get();
        Assert.assertEquals(ByteOrder.LITTLE_ENDIAN, value);
    }

}
