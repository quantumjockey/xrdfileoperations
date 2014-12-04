package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.ByteWrapper;

import java.nio.ByteOrder;

public class UnsignedShortWrapper extends ByteWrapper<Character> {

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

}

