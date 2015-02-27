package edu.hipsec.xrdtiffoperations.imagemodel.martiff.components;

import edu.hipsec.xrdtiffoperations.imagemodel.serialization.ByteSerializer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CalibrationData extends ByteSerializer {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private byte[] coreCalibrationBytes;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public byte[] getCoreCalibrationBytes() {
        return this.coreCalibrationBytes;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public CalibrationData() {
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order) {
        this.coreCalibrationBytes = dataBytes;
    }

    @Override
    public byte[] toByteArray(ByteOrder order) {
        ByteBuffer bytes;
        bytes = ByteBuffer.allocate(this.coreCalibrationBytes.length);
        bytes.order(order);
        bytes.put(this.coreCalibrationBytes);
        return bytes.array();
    }

}
