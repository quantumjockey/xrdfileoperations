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

    private final int INTENSITY_MAXIMUM = Integer.MAX_VALUE;
    private final int INTENSITY_MINIMUM = Integer.MIN_VALUE;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected CalibrationData calibration;
    protected int[][] intensityMap;

    // Added to prevent "variable should be effectively final" compilation errors when passing local variables into lambdas.
    private int intensityMax;
    private int intensityMin;

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
        intensityMax = INTENSITY_MINIMUM;
        cycleImageDataBytes((y, x) -> {
            if (intensityMap[y][x] > intensityMax) {
                intensityMax = intensityMap[y][x];
            }
        });
        return intensityMax;
    }

    public int getMinValue(){
        intensityMin = INTENSITY_MAXIMUM;
        cycleImageDataBytes((y, x) -> {
            if (intensityMap[y][x] < intensityMin) {
                intensityMin = intensityMap[y][x];
            }
        });
        return intensityMin;
    }

    public int getHeight(){
        return intensityMap.length;
    }

    public int getWidth(){
        return intensityMap[0].length;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private byte[] createImageBytes(ByteOrder order){
        int numBytes;
        ByteBuffer bytes;

        numBytes = getIfdListing().get(0).getTagValue(FieldTags.STRIP_BYTE_COUNTS);
        bytes = ByteBuffer.allocate(numBytes);
        bytes.order(order);

        cycleImageDataBytes((y, x) -> {
            bytes.putShort((short) getIntensityMapValue(y, x));
            // risky cast - must determine methods to explicitly prevent buffer overflow without marring data integrity
        });

        return bytes.array();
    }

    private void cycleImageDataBytes(EnvyForCSharpDelegates action){
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                try {
                    action.callMethod(y, x);
                }
                catch (Exception e){
                    System.out.println("Error accessing data at pixel (" + y + "," + x + ").");
                    e.printStackTrace();
                }
            }
        }
    }

    private void getCalibrationData(byte[] fileBytes, ByteOrder _byteOrder){
        int bufferLength, calibrationStartByte, imageStartByte;
        byte[] data;

        calibrationStartByte = searchDirectoriesForTag(FieldTags.CALIBRATION_DATA_OFFSET_SIGNED);
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
        cycleImageDataBytes((y, x) -> {
            intensityMap[y][x] = linearImageArray[x + (y * imageHeight)];
        });
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
        emptyBytes = ByteArray.generateEmptyBytes(imageMetaBytes.length, searchDirectoriesForTag(FieldTags.CALIBRATION_DATA_OFFSET_SIGNED));
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

    /////////// Private Interfaces //////////////////////////////////////////////////////////

    private interface EnvyForCSharpDelegates {
        void callMethod(int a, int b);
    }

}
