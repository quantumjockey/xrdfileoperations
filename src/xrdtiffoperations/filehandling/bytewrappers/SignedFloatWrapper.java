package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.ByteWrapper;
import java.nio.ByteOrder;

public class SignedFloatWrapper extends ByteWrapper<Float> {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedFloatWrapper(ByteOrder order){
        super(Float.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Float get(){
        wrap();
        return buffer.getFloat();
    }

}
