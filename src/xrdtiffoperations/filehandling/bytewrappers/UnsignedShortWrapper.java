package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.WrapperBase;

import java.nio.ByteOrder;

public class UnsignedShortWrapper extends WrapperBase {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public UnsignedShortWrapper(byte[] bytes, ByteOrder order){
        super(bytes, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public char get(){
        return buffer.getChar();
    }

}

