package edu.hipsec.xrdtiffoperations.filehandling.bytewrappers;

import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.base.ByteWrapper;
import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.extensions.IntegerWrapper;
import java.nio.ByteOrder;

public class UnsignedShortWrapper extends ByteWrapper<Character> implements IntegerWrapper {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public UnsignedShortWrapper(ByteOrder order) {
        super(Character.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Character get() {
        wrap();
        return buffer.getChar();
    }

    public int getAsIntPrimitive() {
        return (int) get();
    }

}
