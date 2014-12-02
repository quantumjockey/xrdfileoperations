package xrdtiffoperations.imagemodel.serialization;

import java.nio.ByteOrder;

public interface ByteSerializer {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void fromByteArray(byte[] fieldData, ByteOrder order);
    public byte[] toByteArray(ByteOrder order);

}
