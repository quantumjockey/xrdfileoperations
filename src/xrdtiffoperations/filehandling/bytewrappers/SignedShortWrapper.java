package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.WrapperBase;

import java.nio.ByteOrder;

public class SignedShortWrapper extends WrapperBase {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedShortWrapper(ByteOrder order){
        super(Short.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public short get(){
        wrap();
        return buffer.getShort();
    }

}
