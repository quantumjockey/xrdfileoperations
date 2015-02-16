package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.data.DiffractionFrame;
import xrdtiffoperations.filehandling.bytewrappers.SignedFloatWrapper;
import xrdtiffoperations.filehandling.bytewrappers.SignedIntWrapper;
import xrdtiffoperations.filehandling.bytewrappers.UnsignedShortWrapper;
import xrdtiffoperations.filehandling.bytewrappers.extensions.IntegerWrapper;
import xrdtiffoperations.filehandling.tools.ByteArray;
import xrdtiffoperations.imagemodel.FileTypes;
import xrdtiffoperations.imagemodel.TiffBase;
import xrdtiffoperations.imagemodel.attributes.ResolutionAxis;
import xrdtiffoperations.imagemodel.header.TiffHeader;
import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.ifd.fields.DirectoryField;
import xrdtiffoperations.imagemodel.ifd.fields.FieldTags;
import xrdtiffoperations.imagemodel.ifd.fields.FieldTypes;
import xrdtiffoperations.imagemodel.ifd.fields.SampleTypes;
import xrdtiffoperations.imagemodel.martiff.components.CalibrationData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MARTiffImage extends TiffBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    // Not yet useful, but is being included for storing calibration data bytes
    protected CalibrationData calibration;
    protected DiffractionFrame diffractionData;

    // For indicating file format when converting data to byte array
    private String fileOutputFormat;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public DiffractionFrame getDiffractionData(){
        return diffractionData;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffImage(String _filename) {
        super(_filename);
        calibration = new CalibrationData();
        diffractionData = new DiffractionFrame(_filename);
        fileOutputFormat = FileTypes.TIFF_32_BIT_INT;
    }

    public MARTiffImage(DiffractionFrame data) {
        super(data.getIdentifier());
        calibration = new CalibrationData();
        diffractionData = data;
        fileOutputFormat = FileTypes.TIFF_32_BIT_INT;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

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

        numPixels = diffractionData.getHeight() * diffractionData.getWidth();

        switch (imageType) {
            case FileTypes.TIFF_32_BIT_FLOAT:
                bytes = createByteBuffer(order, numPixels, 4);
                diffractionData.cycleImageDataBytes((y, x) -> bytes.putFloat((float) diffractionData.getIntensityMapValue(y, x)));
                break;
            default: //FileTypes.TIFF_32_BIT_INT:
                bytes = createByteBuffer(order, numPixels, 4);
                diffractionData.cycleImageDataBytes((y, x) -> bytes.putInt(diffractionData.getIntensityMapValue(y, x)));
                break;
        }

        return bytes.array();
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

        diffractionData.initializeIntensityMap(imageHeight, imageWidth);
        diffractionData.setImageXResolution(super.imageXResolution);
        diffractionData.setImageYResolution(super.imageYResolution);

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

        diffractionData.cycleImageDataBytes((y, x) -> {
            System.arraycopy(fileBytes, startingByte + ((x + (y * imageHeight)) * sampleByteLength), pixelTemp.getDataBytes(), 0, sampleByteLength);
            diffractionData.setIntensityMapCoordinate(y, x, pixelTemp.getAsIntPrimitive());
        });
    }

    private void generateNewIfdForImageExport(String imageType, int imageByteCount){
        int bitsPerSample, sampleFormat;

        switch (imageType) {
            case FileTypes.TIFF_32_BIT_FLOAT:
                bitsPerSample = 32;
                sampleFormat = SampleTypes.IEEE_FLOATING_POINT_DATA;
                break;
            default: //FileTypes.TIFF_32_BIT_INT:
                bitsPerSample = 32;
                sampleFormat = SampleTypes.UNSIGNED_INTEGER_DATA;
                break;
        }

        int byteLength = TiffHeader.BYTE_LENGTH + (DirectoryField.BYTE_LENGTH * 12);

        this.getIfdListing().clear();
        this.getIfdListing().add(new ImageFileDirectory());
        this.getIfdListing().get(0).addEntry(FieldTags.IMAGE_WIDTH, FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, 1, diffractionData.getWidth());
        this.getIfdListing().get(0).addEntry(FieldTags.IMAGE_HEIGHT, FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, 1, diffractionData.getHeight());
        this.getIfdListing().get(0).addEntry(FieldTags.BITS_PER_SAMPLE, FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, 1, bitsPerSample);
        this.getIfdListing().get(0).addEntry(FieldTags.COMPRESSION, FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, 1, 1);
        this.getIfdListing().get(0).addEntry(FieldTags.PHOTOMETRIC_INTERPRETATION, FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, 1, 1);
        this.getIfdListing().get(0).addEntry(FieldTags.STRIP_OFFSETS, FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, 1, 512);
        this.getIfdListing().get(0).addEntry(FieldTags.ROWS_PER_STRIP, FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, 1, diffractionData.getHeight());
        this.getIfdListing().get(0).addEntry(FieldTags.STRIP_BYTE_COUNTS, FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, 1, imageByteCount);
        this.getIfdListing().get(0).addEntry(FieldTags.X_RESOLUTION_OFFSET, FieldTypes.RATIONAL, 1, byteLength);
        this.getIfdListing().get(0).addEntry(FieldTags.Y_RESOLUTION_OFFSET, FieldTypes.RATIONAL, 1, byteLength + ResolutionAxis.BYTE_LENGTH);
        this.getIfdListing().get(0).addEntry(FieldTags.RESOLUTION_UNIT, FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, 1, 3);
        this.getIfdListing().get(0).addEntry(FieldTags.SAMPLE_FORMAT, FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, 1, sampleFormat);
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order){
        super.fromByteArray(dataBytes, order);
        if (this.getIfdListing().get(0).getTagValue(FieldTags.CALIBRATION_DATA_OFFSET_SIGNED) != -1){
            getCalibrationData(dataBytes, header.getByteOrder());
        }
        getImageData(dataBytes, header.getByteOrder());
    }

    @Override
    public byte[] toByteArray(ByteOrder order){
        ByteBuffer bytes;
        byte[] emptyBytes, imageDataBytes, imageMetaBytes;
        int totalSize;

        imageDataBytes = createImageBytes(order, this.fileOutputFormat);
        generateNewIfdForImageExport(this.fileOutputFormat, imageDataBytes.length);

        imageMetaBytes = super.toByteArray(order);
        emptyBytes = ByteArray.generateEmptyBytes(imageMetaBytes.length, searchDirectoriesForTag(FieldTags.STRIP_OFFSETS));
        totalSize = imageMetaBytes.length + emptyBytes.length + imageDataBytes.length;

        bytes = ByteBuffer.allocate(totalSize);
        bytes.order(order);
        bytes.put(imageMetaBytes);
        bytes.put(emptyBytes);
        bytes.put(imageDataBytes);

        return bytes.array();
    }

}
