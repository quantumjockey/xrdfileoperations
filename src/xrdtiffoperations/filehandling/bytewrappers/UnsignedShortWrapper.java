package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.ByteWrapper;

import java.nio.ByteOrder;

public class UnsignedShortWrapper extends ByteWrapper<Character> {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int MAX_BOUND = 65535;
    private final int MIN_BOUND = 0;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public UnsignedShortWrapper(ByteOrder order){
        super(Character.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Character get(){
        wrap();
        return buffer.getChar();
    }

    public int getAsInt(){
        int input = (int)this.get();

        if (input > MAX_BOUND) return MAX_BOUND;
        if (input < MIN_BOUND) return MIN_BOUND;

        return input;
    }

}

