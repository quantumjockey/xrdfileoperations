package edu.hipsec.xrdtiffoperations.filehandling.bytewrappers;

import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.base.ByteWrapper;
import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.extensions.IntegerWrapper;
import java.nio.ByteOrder;

public class SignedFloatWrapper extends ByteWrapper<Float> implements IntegerWrapper {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedFloatWrapper(ByteOrder order) {
        super(Float.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Float get() {
        wrap();
        return buffer.getFloat();
    }

    public int getAsIntPrimitive() {
        return Math.round(get());
    }

}
