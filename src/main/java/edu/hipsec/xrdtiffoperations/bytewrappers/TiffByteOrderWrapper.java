package edu.hipsec.xrdtiffoperations.bytewrappers;

import edu.hipsec.xrdtiffoperations.bytewrappers.base.ByteData;
import java.nio.ByteOrder;

public class TiffByteOrderWrapper extends ByteData {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffByteOrderWrapper() {
        super(2);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public ByteOrder get() {
        return this.getByteOrder(new String(this.dataBytes));
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private ByteOrder getByteOrder(String orderId) {
        ByteOrder order;

        switch (orderId) {
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
