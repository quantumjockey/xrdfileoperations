package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.ByteWrapper;
import java.nio.ByteOrder;

public class SignedLongWrapper extends ByteWrapper {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public SignedLongWrapper(ByteOrder order){
        super(Long.SIZE, order);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public int get(){
        wrap();
        return buffer.getInt();
    }

}
