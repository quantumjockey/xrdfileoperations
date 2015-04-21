package edu.hipsec.xrdtiffoperations.bytewrappers;

import edu.hipsec.xrdtiffoperations.bytewrappers.base.ByteWrapper;
import edu.hipsec.xrdtiffoperations.bytewrappers.extensions.IntegerWrapper;
import java.nio.ByteOrder;

public class UnsignedShortWrapper extends ByteWrapper<Character> implements IntegerWrapper {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public UnsignedShortWrapper(ByteOrder order) {
        super(Character.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Character get() {
        this.wrap();
        return this.buffer.getChar();
    }

    public int getAsIntPrimitive() {
        return (int) this.get();
    }

}
