package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.ByteWrapper;

import java.nio.ByteOrder;

public class SignedShortWrapper extends ByteWrapper<Short> {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedShortWrapper(ByteOrder order){
        super(Short.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Short get(){
        wrap();
        return buffer.getShort();
    }

}
