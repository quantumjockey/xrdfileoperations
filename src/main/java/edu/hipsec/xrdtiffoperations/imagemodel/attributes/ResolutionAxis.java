package edu.hipsec.xrdtiffoperations.imagemodel.attributes;

import edu.hipsec.xrdtiffoperations.imagemodel.serialization.ByteSerializer;
import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.SignedIntWrapper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ResolutionAxis extends ByteSerializer {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    public static final int BYTE_LENGTH = 8;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private int denominator;
    private int numerator;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public int getDenominator() {
        return denominator;
    }

    public int getNumerator() {
        return numerator;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ResolutionAxis() {
        numerator = 0;
        denominator = 1;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public double getValue() {
        return (double) numerator / (double) denominator;
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order) {
        SignedIntWrapper denum, num;
        int valueLength;

        if (dataBytes.length == BYTE_LENGTH) {
            valueLength = BYTE_LENGTH / 2;

            num = new SignedIntWrapper(order);
            System.arraycopy(dataBytes, 0, num.getDataBytes(), 0, valueLength);
            numerator = num.get();

            denum = new SignedIntWrapper(order);
            System.arraycopy(dataBytes, valueLength, denum.getDataBytes(), 0, valueLength);
            denominator = denum.get();
        } else
            displaySizeAlert(dataBytes.length, BYTE_LENGTH);
    }

    @Override
    public byte[] toByteArray(ByteOrder order) {
        ByteBuffer bytes;
        bytes = ByteBuffer.allocate(BYTE_LENGTH);
        bytes.order(order);
        bytes.putInt(numerator);
        bytes.putInt(denominator);
        return bytes.array();
    }

}
