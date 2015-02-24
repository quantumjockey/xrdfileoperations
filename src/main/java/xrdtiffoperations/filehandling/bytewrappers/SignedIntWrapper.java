package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.ByteWrapper;
import xrdtiffoperations.filehandling.bytewrappers.extensions.IntegerWrapper;
import java.nio.ByteOrder;

public class SignedIntWrapper extends ByteWrapper<Integer> implements IntegerWrapper {

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

    public int getAsIntPrimitive(){
        return get();
    }

}
