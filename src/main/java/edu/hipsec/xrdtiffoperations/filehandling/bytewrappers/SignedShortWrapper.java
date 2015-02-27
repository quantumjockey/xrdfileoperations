package edu.hipsec.xrdtiffoperations.filehandling.bytewrappers;

import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.base.ByteWrapper;
import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.extensions.IntegerWrapper;

import java.nio.ByteOrder;

public class SignedShortWrapper extends ByteWrapper<Short> implements IntegerWrapper {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedShortWrapper(ByteOrder order) {
        super(Short.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Short get() {
        this.wrap();
        return this.buffer.getShort();
    }

    public int getAsIntPrimitive() {
        return (int) this.get();
    }

    @Override
    public byte[] getDataBytes() {
        return super.getDataBytes();
    }

}
