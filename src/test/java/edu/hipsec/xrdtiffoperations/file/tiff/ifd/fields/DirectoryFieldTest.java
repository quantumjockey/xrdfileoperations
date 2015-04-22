package edu.hipsec.xrdtiffoperations.file.tiff.ifd.fields;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.nio.ByteOrder;

public class DirectoryFieldTest {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    DirectoryField field;

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        this.field = new DirectoryField();
    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void fromByteArray_sourceArrayEmpty_conversionUnsuccessful() {
        byte[] test = new byte[DirectoryField.BYTE_LENGTH];
        Assert.assertFalse(this.field.fromByteArray(test, ByteOrder.BIG_ENDIAN));
    }

    @Test
    public void fromByteArray_sourceArraySmallerThanExpected_conversionUnsuccessful() {
        byte[] test = new byte[3];
        for (int i = 0; i < test.length; i++)
            test[i] = (byte)i;
        Assert.assertFalse(this.field.fromByteArray(test, ByteOrder.BIG_ENDIAN));
    }

    @Test
    public void toByteArray_arrayGenerated_arraySizeSumOfExpectedFieldTypes() {
        Assert.assertEquals(12, this.field.toByteArray(ByteOrder.BIG_ENDIAN).length);
    }

}
