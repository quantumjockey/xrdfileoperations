package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.filehandling.tools.ByteArray;
import xrdtiffoperations.imagemodel.TiffBase;
import xrdtiffoperations.imagemodel.ifd.fields.FieldTags;
import xrdtiffoperations.imagemodel.martiff.components.CalibrationData;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MARTiffImage extends TiffBase {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int INTENSITY_MAXIMUM = 65537;
    private final int INTENSITY_MINIMUM = 0;
    private final short CALIBRATION_OFFSET_SIGNED = -30826;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected CalibrationData calibration;
    protected int[][] intensityMap;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public CalibrationData getCalibration(){
        return calibration;
    }

    public int getIntensityMapValue(int y, int x){
        return intensityMap[y][x];
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffImage(String _filename) {
        super(_filename);
        calibration = new CalibrationData();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public int getMaxValue(){
        int maxVal = INTENSITY_MINIMUM;
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                if (intensityMap[y][x] > maxVal){
                    maxVal = intensityMap[y][x];
                }
            }
        }
        return maxVal;
    }

    public int getMinValue(){
        int minVal = INTENSITY_MAXIMUM;
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                if (intensityMap[y][x] < minVal){
                    minVal = intensityMap[y][x];
                }
            }
        }
        return minVal;
    }

    public int getHeight(){
        return intensityMap.length;
    }

    public int getWidth(){
        return intensityMap[0].length;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void getCalibrationData(byte[] fileBytes, ByteOrder _byteOrder){
        int bufferLength, calibrationStartByte, imageStartByte;
        byte[] data;

        calibrationStartByte = searchDirectoriesForTag(CALIBRATION_OFFSET_SIGNED);
        imageStartByte = searchDirectoriesForTag(FieldTags.STRIP_OFFSETS);
        bufferLength = imageStartByte - calibrationStartByte;
        data = new byte[bufferLength];
        System.arraycopy(fileBytes, calibrationStartByte, data, 0, bufferLength);

        calibration.fromByteArray(data, _byteOrder);
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order){
        super.fromByteArray(dataBytes, order);
        getCalibrationData(dataBytes, order);
    }

    @Override
    public byte[] toByteArray(ByteOrder order){
        ByteBuffer bytes;
        byte[] calibrationBytes, emptyBytes, imageMetaBytes;
        int totalSize;

        imageMetaBytes = super.toByteArray(order);
        emptyBytes = ByteArray.generateEmptyBytes(imageMetaBytes.length, searchDirectoriesForTag(CALIBRATION_OFFSET_SIGNED));
        calibrationBytes = calibration.toByteArray(order);

        totalSize = imageMetaBytes.length + emptyBytes.length + calibrationBytes.length;

        bytes = ByteBuffer.allocate(totalSize);
        bytes.order(order);
        bytes.put(imageMetaBytes);
        bytes.put(emptyBytes);
        bytes.put(calibrationBytes);

        return bytes.array();
    }

}
