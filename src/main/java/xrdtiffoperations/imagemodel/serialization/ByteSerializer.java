package xrdtiffoperations.imagemodel.serialization;

import java.nio.ByteOrder;

public abstract class ByteSerializer {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public abstract void fromByteArray(byte[] dataBytes, ByteOrder order);

    public abstract byte[] toByteArray(ByteOrder order);

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    protected void displaySizeAlert(int actualSize, int specSize) {
        System.out.println("Byte array is " + actualSize + " bytes and not of proper length.");
        System.out.println("Array must contain exactly " + specSize + " bytes to accord with TIFF 2.0 Revision 6.0 specification.");
    }

}
