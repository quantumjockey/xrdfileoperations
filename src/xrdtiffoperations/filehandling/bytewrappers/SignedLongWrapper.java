package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.WrapperBase;
import java.nio.ByteOrder;

public class SignedLongWrapper extends WrapperBase {

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
