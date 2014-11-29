package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.WrapperBase;

import java.nio.ByteOrder;

public class SignedIntWrapper extends WrapperBase {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedIntWrapper(ByteOrder order){
        super(Integer.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public int get(){
        wrap();
        return buffer.getInt();
    }

}
