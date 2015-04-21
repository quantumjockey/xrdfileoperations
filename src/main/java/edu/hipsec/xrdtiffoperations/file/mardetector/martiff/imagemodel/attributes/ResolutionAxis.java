package edu.hipsec.xrdtiffoperations.file.mardetector.martiff.imagemodel.attributes;

import edu.hipsec.xrdtiffoperations.utilities.bytes.ByteSerializer;
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
        return this.denominator;
    }

    public int getNumerator() {
        return this.numerator;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ResolutionAxis() {
        this.numerator = 0;
        this.denominator = 1;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public double getValue() {
        return (double) this.numerator / (double) this.denominator;
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
            this.numerator = num.get();

            denum = new SignedIntWrapper(order);
            System.arraycopy(dataBytes, valueLength, denum.getDataBytes(), 0, valueLength);
            this.denominator = denum.get();
        } else
            this.displaySizeAlert(dataBytes.length, BYTE_LENGTH);
    }

    @Override
    public byte[] toByteArray(ByteOrder order) {
        ByteBuffer bytes;
        bytes = ByteBuffer.allocate(BYTE_LENGTH);
        bytes.order(order);
        bytes.putInt(this.numerator);
        bytes.putInt(this.denominator);
        return bytes.array();
    }

}
