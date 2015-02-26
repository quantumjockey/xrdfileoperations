package edu.hipsec.xrdtiffoperations.imagemodel.martiff.components;

import edu.hipsec.xrdtiffoperations.imagemodel.serialization.ByteSerializer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CalibrationData extends ByteSerializer {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private byte[] coreCalibrationBytes;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public byte[] getCoreCalibrationBytes() {
        return coreCalibrationBytes;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public CalibrationData() {
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order) {
        coreCalibrationBytes = dataBytes;
    }

    @Override
    public byte[] toByteArray(ByteOrder order) {
        ByteBuffer bytes;
        bytes = ByteBuffer.allocate(coreCalibrationBytes.length);
        bytes.order(order);
        bytes.put(coreCalibrationBytes);
        return bytes.array();
    }

}
