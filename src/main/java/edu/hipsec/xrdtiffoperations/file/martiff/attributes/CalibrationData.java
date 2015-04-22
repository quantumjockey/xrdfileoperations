package edu.hipsec.xrdtiffoperations.file.martiff.attributes;

import edu.hipsec.xrdtiffoperations.utilities.bytes.ByteSerializer;
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
    public boolean fromByteArray(byte[] dataBytes, ByteOrder order) {
        this.coreCalibrationBytes = dataBytes;
        return true;
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
