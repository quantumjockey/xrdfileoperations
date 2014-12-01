package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.ByteWrapper;

import java.nio.ByteOrder;

public class SignedIntWrapper extends ByteWrapper<Integer> {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedIntWrapper(ByteOrder order){
        super(Integer.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Integer get(){
        wrap();
        return buffer.getInt();
    }

}
