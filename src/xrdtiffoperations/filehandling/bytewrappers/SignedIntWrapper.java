package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.WrapperBase;

import java.nio.ByteOrder;

public class SignedIntWrapper extends WrapperBase {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedIntWrapper(byte[] bytes, ByteOrder order){
        super(bytes ,order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public int get(){
        return buffer.getInt();
    }

}
