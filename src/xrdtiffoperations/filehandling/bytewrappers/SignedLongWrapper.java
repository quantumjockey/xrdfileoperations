package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.ByteWrapper;
import java.nio.ByteOrder;

public class SignedLongWrapper extends ByteWrapper<Integer> {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedLongWrapper(ByteOrder order){
        super(Long.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Integer get(){
        wrap();
        return buffer.getInt();
    }

}
