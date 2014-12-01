package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.ByteWrapper;

import java.nio.ByteOrder;

public class UnsignedShortWrapper extends ByteWrapper {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int MAX_BOUND = 65535;
    private final int MIN_BOUND = 0;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public UnsignedShortWrapper(ByteOrder order){
        super(Character.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public int get(){
        wrap();
        int input = (int)buffer.getChar();

        if (input > MAX_BOUND) return MAX_BOUND;
        if (input < MIN_BOUND) return MIN_BOUND;

        return input;
    }

}

