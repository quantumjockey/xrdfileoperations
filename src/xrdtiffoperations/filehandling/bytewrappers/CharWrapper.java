package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.WrapperBase;

import java.nio.ByteOrder;

public class CharWrapper extends WrapperBase {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public CharWrapper(byte[] bytes, ByteOrder order){
        super(bytes, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public char get(){
        return buffer.getChar();
    }

}

