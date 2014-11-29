package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.WrapperBase;

import java.nio.ByteOrder;

public class SignedShortWrapper extends WrapperBase {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedShortWrapper(byte[] bytes, ByteOrder order){
        super(bytes, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public short get(){
        return buffer.getShort();
    }

}
