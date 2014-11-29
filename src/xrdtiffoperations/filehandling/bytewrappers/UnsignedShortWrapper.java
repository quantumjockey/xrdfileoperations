package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.WrapperBase;

import java.nio.ByteOrder;

public class UnsignedShortWrapper extends WrapperBase {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int MAX_BOUND = 65535;
    private final int MIN_BOUND = 0;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public UnsignedShortWrapper(byte[] bytes, ByteOrder order){
        super(bytes, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public int get(){
        int input = (int)buffer.getChar();

        if (input > MAX_BOUND) return MAX_BOUND;
        if (input < MIN_BOUND) return MIN_BOUND;

        return input;
    }

}

