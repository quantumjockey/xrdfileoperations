package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.filehandling.bytewrappers.SignedFloatWrapper;
import xrdtiffoperations.filehandling.bytewrappers.SignedIntWrapper;
import xrdtiffoperations.filehandling.bytewrappers.UnsignedShortWrapper;
import xrdtiffoperations.filehandling.bytewrappers.extensions.IntegerWrapper;
import xrdtiffoperations.filehandling.tools.ByteArray;
import xrdtiffoperations.imagemodel.FileTypes;
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

    // For indicating file format when converting data to byte array
    private String fileOutputFormat;

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
        fileOutputFormat = FileTypes.TIFF_32_BIT_INT;
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

    public void setFileOutputFormat(String fileType){
        this.fileOutputFormat = fileType;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private ByteBuffer createByteBuffer(ByteOrder order, int numPixels, int bytesPerPixel){
        ByteBuffer buffer;
        int numBytes;

        numBytes = numPixels * bytesPerPixel;
        buffer = ByteBuffer.allocate(numBytes);
        buffer.order(order);

        return buffer;
    }

    private byte[] createImageBytes(ByteOrder order, String imageType){
        ByteBuffer bytes;
        int numPixels;

        numPixels = getHeight() * getWidth();

        switch (imageType) {
            case FileTypes.TIFF_8_BIT_INT:
                bytes = createByteBuffer(order, numPixels, 1);
                cycleImageDataBytes((y, x) -> bytes.put(scaleDataToByte(getIntensityMapValue(y, x))));
                break;
            case FileTypes.TIFF_16_BIT_INT:
                bytes = createByteBuffer(order, numPixels, 2);
                cycleImageDataBytes((y, x) -> bytes.putChar(scaleDataToUnsignedShort(getIntensityMapValue(y, x))));
                break;
            case FileTypes.TIFF_32_BIT_FLOAT:
                bytes = createByteBuffer(order, numPixels, 4);
                cycleImageDataBytes((y, x) -> bytes.putFloat((float)getIntensityMapValue(y, x)));
                break;
            default: //FileTypes.TIFF_32_BIT_INT:
                bytes = createByteBuffer(order, numPixels, 4);
                cycleImageDataBytes((y, x) -> bytes.putInt(getIntensityMapValue(y, x)));
                break;
        }

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

        switch (sampleByteLength){
            case 4:
                pixelTemp = (sampleType == SampleTypes.IEEE_FLOATING_POINT_DATA)
                        ? new SignedFloatWrapper(_byteOrder)
                        : new SignedIntWrapper(_byteOrder);
                break;
            case 2:
                pixelTemp = new UnsignedShortWrapper(_byteOrder);
                break;
            default:
                pixelTemp = new SignedIntWrapper(_byteOrder);
                break;
        }

        cycleImageDataBytes((y, x) -> {
            System.arraycopy(fileBytes, startingByte + ((x + (y * imageHeight)) * sampleByteLength), pixelTemp.getDataBytes(), 0, sampleByteLength);
            intensityMap[y][x] = pixelTemp.getAsIntPrimitive();
        });
    }

    private byte scaleDataToByte(int dataValue){
        byte scaledValue;
        float scale;

        scale = (dataValue - getMinValue()) / (getMaxValue() - getMinValue());
        scaledValue = (byte)(scale * (float)Byte.MAX_VALUE);

        return scaledValue;
    }

    private char scaleDataToUnsignedShort(int dataValue){
        char scaledValue;
        float scale;

        scale = (dataValue - getMinValue()) / (getMaxValue() - getMinValue());
        scaledValue = (char)(scale * (float)Character.MAX_VALUE);

        return scaledValue;
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
        byte[] emptyBytes, imageDataBytes, imageMetaBytes;
        int totalSize;

        imageDataBytes = createImageBytes(order, this.fileOutputFormat);

        imageMetaBytes = super.toByteArray(order);
        emptyBytes = ByteArray.generateEmptyBytes(imageMetaBytes.length, searchDirectoriesForTag(FieldTags.CALIBRATION_DATA_OFFSET_SIGNED));

        totalSize = imageMetaBytes.length + emptyBytes.length + imageDataBytes.length;

        bytes = ByteBuffer.allocate(totalSize);
        bytes.order(order);
        bytes.put(imageMetaBytes);
        bytes.put(emptyBytes);
        bytes.put(imageDataBytes);

        return bytes.array();
    }

    /////////// Private Interfaces //////////////////////////////////////////////////////////

    private interface EnvyForCSharpDelegates {
        void callMethod(int a, int b);
    }

}
