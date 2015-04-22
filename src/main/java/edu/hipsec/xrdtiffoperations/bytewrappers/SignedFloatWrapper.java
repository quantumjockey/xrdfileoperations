package edu.hipsec.xrdtiffoperations.bytewrappers;

import edu.hipsec.xrdtiffoperations.bytewrappers.base.ByteWrapper;
import edu.hipsec.xrdtiffoperations.bytewrappers.extensions.IntegerWrapper;
import java.nio.ByteOrder;

public class SignedFloatWrapper extends ByteWrapper<Float> implements IntegerWrapper {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedFloatWrapper(ByteOrder order) {
        super(Float.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Float get() {
        this.wrap();
        return this.buffer.getFloat();
    }

    public int getAsIntPrimitive() {
        return Math.round(this.get());
    }

}
