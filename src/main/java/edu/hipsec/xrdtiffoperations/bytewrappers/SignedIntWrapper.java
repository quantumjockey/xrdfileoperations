package edu.hipsec.xrdtiffoperations.bytewrappers;

import edu.hipsec.xrdtiffoperations.bytewrappers.base.ByteWrapper;
import edu.hipsec.xrdtiffoperations.bytewrappers.extensions.IntegerWrapper;

import java.nio.ByteOrder;

public class SignedIntWrapper extends ByteWrapper<Integer> implements IntegerWrapper {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedIntWrapper(ByteOrder order) {
        super(Integer.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Integer get() {
        this.wrap();
        return this.buffer.getInt();
    }

    public int getAsIntPrimitive() {
        return this.get();
    }

}
