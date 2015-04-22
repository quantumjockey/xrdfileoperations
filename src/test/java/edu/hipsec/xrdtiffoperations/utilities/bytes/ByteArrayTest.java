package edu.hipsec.xrdtiffoperations.utilities.bytes;

import org.junit.Assert;
import org.junit.Test;

public class ByteArrayTest {

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void generateEmptyBytes_inputRegionLengthGreaterThanZero_everyElementIsZeroByte() {
        byte[] temp = ByteArray.generateEmptyBytes(3, 9);
        for (byte element : temp){
            Assert.assertEquals((byte)0, element);
        }
    }

    @Test
    public void generateEmptyBytes_inputRegionLengthGreaterThanZero_arrayWithRegionLength() {
        Assert.assertEquals(6, ByteArray.generateEmptyBytes(3, 9).length);
    }

    @Test
    public void generateEmptyBytes_inputOutOfOrder_emptyArray() {
        Assert.assertEquals(0, ByteArray.generateEmptyBytes(9, 3).length);
    }

}
