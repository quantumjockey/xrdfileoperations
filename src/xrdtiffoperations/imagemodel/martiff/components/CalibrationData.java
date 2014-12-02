package xrdtiffoperations.imagemodel.martiff.components;

import java.nio.ByteOrder;

public class CalibrationData {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private byte[] coreCalibrationBytes;
    private ResolutionAxis detectorXResolution;
    private ResolutionAxis detectorYResolution;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public byte[] getCoreCalibrationBytes(){
        return coreCalibrationBytes;
    }

    public ResolutionAxis getDetectorXResolution(){
        return detectorXResolution;
    }

    public ResolutionAxis getDetectorYResolution(){
        return detectorYResolution;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public CalibrationData(int ifdEndByte, int xResByte, int yResByte, int calibrationStartByte, byte[] dataBuffer, ByteOrder order) {
        int relativeCalibrationOffset, relativeXResOffset, relativeYResOffset;

        relativeXResOffset = xResByte - ifdEndByte;
        relativeYResOffset = yResByte - ifdEndByte;
        getDetectorResolution(dataBuffer, relativeXResOffset, relativeYResOffset, order);

        relativeCalibrationOffset = calibrationStartByte - ifdEndByte;
        getCoreCalibrationData(dataBuffer, relativeCalibrationOffset, order);
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void getCoreCalibrationData(byte[] buffer, int calOffset, ByteOrder order){
        byte[] calibrationBytes;
        int calibrationLength;

        calibrationLength = buffer.length - calOffset;
        calibrationBytes = new byte[calibrationLength];
        System.arraycopy(buffer, calOffset, calibrationBytes, 0, calibrationLength);
        // Encoding for detector outputs required before this can properly read
        // central section reads when UTF-16 charset selected
        coreCalibrationBytes = calibrationBytes;
    }

    private void getDetectorResolution(byte[] buffer, int relX, int relY, ByteOrder order){
        byte[] xRes, yRes;

        xRes = new byte[ResolutionAxis.BYTE_LENGTH];
        System.arraycopy(buffer, relX, xRes, 0, ResolutionAxis.BYTE_LENGTH);
        detectorXResolution = new ResolutionAxis();
        detectorXResolution.fromByteArray(xRes, order);

        yRes = new byte[ResolutionAxis.BYTE_LENGTH];
        System.arraycopy(buffer, relY, yRes, 0, ResolutionAxis.BYTE_LENGTH);
        detectorYResolution = new ResolutionAxis();
        detectorXResolution.fromByteArray(yRes, order);
    }

}
