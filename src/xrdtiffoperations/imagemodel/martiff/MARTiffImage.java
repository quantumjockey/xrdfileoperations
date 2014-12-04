package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.filehandling.bytewrappers.SignedFloatWrapper;
import xrdtiffoperations.filehandling.bytewrappers.SignedIntWrapper;
import xrdtiffoperations.filehandling.bytewrappers.UnsignedShortWrapper;
import xrdtiffoperations.filehandling.bytewrappers.extensions.IntegerWrapper;
import xrdtiffoperations.filehandling.tools.ByteArray;
import xrdtiffoperations.imagemodel.TiffBase;
import xrdtiffoperations.imagemodel.ifd.fields.FieldTags;
import xrdtiffoperations.imagemodel.ifd.fields.SampleTypes;
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
        IntegerWrapper pixelTemp;
        int startingByte;
        int imageHeight, imageWidth;
        int sampleByteLength, sampleType;

        imageHeight = searchDirectoriesForTag(FieldTags.IMAGE_HEIGHT);
        imageWidth = searchDirectoriesForTag(FieldTags.IMAGE_WIDTH);
        startingByte = searchDirectoriesForTag(FieldTags.STRIP_OFFSETS);
        sampleByteLength = searchDirectoriesForTag(FieldTags.BITS_PER_SAMPLE) / Byte.SIZE;
        sampleType = searchDirectoriesForTag(FieldTags.SAMPLE_FORMAT);

        intensityMap = new int[imageHeight][imageWidth];

        if (sampleByteLength == 4 && sampleType == SampleTypes.IEEE_FLOATING_POINT_DATA){
            pixelTemp = new SignedFloatWrapper(_byteOrder);
        }
        else if (sampleByteLength == 2){
            pixelTemp = new UnsignedShortWrapper(_byteOrder);
        }
        else {
            pixelTemp = new SignedIntWrapper(_byteOrder);
        }

        cycleImageDataBytes((y, x) -> {
            System.arraycopy(fileBytes, startingByte + ((x + (y * imageHeight)) * sampleByteLength), pixelTemp.getDataBytes(), 0, sampleByteLength);
            intensityMap[y][x] = pixelTemp.getAsIntPrimitive();
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
