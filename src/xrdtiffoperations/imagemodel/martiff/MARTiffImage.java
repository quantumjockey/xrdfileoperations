package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.filehandling.bytewrappers.UnsignedShortWrapper;
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

    // (FOR INTEGRITY TESTING ONLY) //
    private final int FIT2D_IMAGE_DATA_STARTING_BYTE_READ_SHIFT = -2;

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

    private byte[] createImageBytes(ByteOrder order){
        int gridHeight, gridWidth, numBytes;
        ByteBuffer bytes;

        numBytes = getIfdListing().get(0).getTagValue(FieldTags.STRIP_BYTE_COUNTS);
        bytes = ByteBuffer.allocate(numBytes);
        bytes.order(order);
        gridHeight = getHeight();
        gridWidth = getWidth();

        for (int y = 0; y < gridHeight; y++){
            for (int x = 0; x < gridWidth; x++){
                bytes.putShort((short)getIntensityMapValue(y, x));
                // risky cast - must determine methods to explicitly prevent buffer overflow without marring data integrity
            }
        }

        return bytes.array();
    }

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

    private void getImageData(byte[] fileBytes, ByteOrder _byteOrder){
        int[] linearImageArray;
        UnsignedShortWrapper pixelTemp;
        int z;
        int dataLength, startingByte;
        int imageHeight, imageWidth;

        dataLength = searchDirectoriesForTag(FieldTags.STRIP_BYTE_COUNTS);
        imageHeight = searchDirectoriesForTag(FieldTags.IMAGE_HEIGHT);
        imageWidth = searchDirectoriesForTag(FieldTags.IMAGE_WIDTH);
        startingByte = searchDirectoriesForTag(FieldTags.STRIP_OFFSETS);

        linearImageArray = new int[imageHeight * imageWidth];
        pixelTemp = new UnsignedShortWrapper(_byteOrder);
        z = 0;

        for(int i = 0; i < (dataLength); i++){
            if ((startingByte + i ) % 2 == 0){
                pixelTemp.getDataBytes()[0] = fileBytes[startingByte + i];
            }
            else if ((startingByte + i ) % 2 != 0) {
                pixelTemp.getDataBytes()[1] = fileBytes[startingByte + i];
                linearImageArray[z] = pixelTemp.getAsInt();
                z++;
            }
        }
        intensityMap = new int[imageHeight][imageWidth];
        for (int y = 0; y < imageHeight; y++){
            for (int x = 0; x < imageWidth; x++){
                intensityMap[y][x] = linearImageArray[x + (y * imageHeight)];
            }
        }
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order){
        super.fromByteArray(dataBytes, order);
        getCalibrationData(dataBytes, order);
        getImageData(dataBytes, order);
    }

    @Override
    public byte[] toByteArray(ByteOrder order){
        ByteBuffer bytes;
        byte[] calibrationBytes, emptyBytes, imageDataBytes, imageMetaBytes;
        int totalSize;

        imageMetaBytes = super.toByteArray(order);
        emptyBytes = ByteArray.generateEmptyBytes(imageMetaBytes.length, searchDirectoriesForTag(CALIBRATION_OFFSET_SIGNED));
        calibrationBytes = calibration.toByteArray(order);
        imageDataBytes = createImageBytes(order);

        totalSize = imageMetaBytes.length + emptyBytes.length + calibrationBytes.length + imageDataBytes.length;

        bytes = ByteBuffer.allocate(totalSize);
        bytes.order(order);
        bytes.put(imageMetaBytes);
        bytes.put(emptyBytes);
        bytes.put(calibrationBytes);
        bytes.put(imageDataBytes);

        return bytes.array();
    }

}
