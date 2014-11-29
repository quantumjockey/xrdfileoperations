package xrdtiffoperations.filehandling.bytewrappers;

import xrdtiffoperations.filehandling.bytewrappers.base.WrapperBase;
import java.nio.ByteOrder;

public class TiffByteOrderWrapper extends WrapperBase {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffByteOrderWrapper(){
        super(16, ByteOrder.nativeOrder());
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public ByteOrder get(){
        return getByteOrder(new String(dataBytes));
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private ByteOrder getByteOrder(String orderId){
        ByteOrder order;

        switch (orderId){
            case "II":
                order = ByteOrder.LITTLE_ENDIAN;
                break;
            case "MM":
                order = ByteOrder.BIG_ENDIAN;
                break;
            default:
                order = ByteOrder.nativeOrder();
        }

        return order;
    }

}
