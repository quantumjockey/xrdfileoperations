package edu.hipsec.xrdtiffoperations.file.martiff;

import edu.hipsec.xrdtiffoperations.file.tiff.ifd.fields.SampleTypes;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrdtiffoperations.bytewrappers.SignedFloatWrapper;
import edu.hipsec.xrdtiffoperations.bytewrappers.SignedIntWrapper;
import edu.hipsec.xrdtiffoperations.bytewrappers.UnsignedShortWrapper;
import edu.hipsec.xrdtiffoperations.bytewrappers.extensions.IntegerWrapper;
import edu.hipsec.xrdtiffoperations.file.tiff.TiffR6Image;
import edu.hipsec.xrdtiffoperations.file.martiff.imagemodel.attributes.CalibrationData;
import edu.hipsec.xrdtiffoperations.file.martiff.imagemodel.attributes.ResolutionAxis;
import edu.hipsec.xrdtiffoperations.file.tiff.header.TiffHeader;
import edu.hipsec.xrdtiffoperations.file.tiff.ifd.ImageFileDirectory;
import edu.hipsec.xrdtiffoperations.file.tiff.ifd.fields.DirectoryField;
import edu.hipsec.xrdtiffoperations.file.tiff.ifd.fields.FieldTags;
import edu.hipsec.xrdtiffoperations.file.tiff.ifd.fields.FieldTypes;
import edu.hipsec.xrdtiffoperations.utilities.bytes.ByteArray;
import edu.hipsec.xrdtiffoperations.constants.FileTypes;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MARTiffImage extends TiffR6Image {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    // Not yet useful, but is being included for storing calibration data bytes
    protected CalibrationData calibration;
    protected DiffractionFrame diffractionData;

    // For indicating file format when converting data to byte array
    private String fileOutputFormat;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public DiffractionFrame getDiffractionData() {
        return this.diffractionData;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffImage(String _filename) {
        super(_filename);
        this.initializeImageObject(new DiffractionFrame(_filename));
    }

    public MARTiffImage(DiffractionFrame data) {
        super(data.getIdentifier());
        this.initializeImageObject(data);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void setFileOutputFormat(String fileType) {
        this.fileOutputFormat = fileType;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private ByteBuffer createByteBuffer(ByteOrder order, int numPixels, int bytesPerPixel) {
        ByteBuffer buffer;
        int numBytes;

        numBytes = numPixels * bytesPerPixel;
        buffer = ByteBuffer.allocate(numBytes);
        buffer.order(order);

        return buffer;
    }

    private byte[] createImageBytes(ByteOrder order, String imageType) {
        ByteBuffer bytes;
        int numPixels;

        numPixels = this.diffractionData.getHeight() * this.diffractionData.getWidth();

        switch (imageType) {
            case FileTypes.TIFF_32_BIT_FLOAT:
                bytes = this.createByteBuffer(order, numPixels, 4);
                this.diffractionData.cycleFramePixels((y, x) -> bytes.putFloat((float) this.diffractionData.getIntensityMapValue(y, x)));
                break;
            default: //FileTypes.TIFF_32_BIT_INT:
                bytes = this.createByteBuffer(order, numPixels, 4);
                this.diffractionData.cycleFramePixels((y, x) -> bytes.putInt(this.diffractionData.getIntensityMapValue(y, x)));
                break;
        }

        return bytes.array();
    }

    private void getCalibrationData(byte[] fileBytes, ByteOrder _byteOrder) {
        int bufferLength, calibrationStartByte, imageStartByte;
        byte[] data;

        calibrationStartByte = this.searchDirectoriesForTag(FieldTags.CALIBRATION_DATA_OFFSET_SIGNED);
        imageStartByte = this.searchDirectoriesForTag(FieldTags.STRIP_OFFSETS);
        bufferLength = imageStartByte - calibrationStartByte;
        data = new byte[bufferLength];
        System.arraycopy(fileBytes, calibrationStartByte, data, 0, bufferLength);

        this.calibration.fromByteArray(data, _byteOrder);
    }

    private void getImageData(byte[] fileBytes, ByteOrder _byteOrder) {
        IntegerWrapper pixelTemp;
        int startingByte;
        int imageHeight, imageWidth;
        int sampleByteLength, sampleType;

        imageHeight = this.searchDirectoriesForTag(FieldTags.IMAGE_HEIGHT);
        imageWidth = this.searchDirectoriesForTag(FieldTags.IMAGE_WIDTH);
        startingByte = this.searchDirectoriesForTag(FieldTags.STRIP_OFFSETS);
        sampleByteLength = this.searchDirectoriesForTag(FieldTags.BITS_PER_SAMPLE) / Byte.SIZE;
        sampleType = this.searchDirectoriesForTag(FieldTags.SAMPLE_FORMAT);

        this.diffractionData.initializeIntensityMap(imageHeight, imageWidth);
        this.diffractionData.setImageXResolution(super.imageXResolution);
        this.diffractionData.setImageYResolution(super.imageYResolution);

        switch (sampleByteLength) {
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

        this.diffractionData.cycleFramePixels((y, x) -> {
            System.arraycopy(fileBytes, startingByte + ((x + (y * imageHeight)) * sampleByteLength), pixelTemp.getDataBytes(), 0, sampleByteLength);
            this.diffractionData.setIntensityMapCoordinate(y, x, pixelTemp.getAsIntPrimitive());
        });
    }

    private void generateNewIfdForImageExport(String imageType, int imageByteCount) {
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
        this.getIfdListing().get(0).addEntry(FieldTags.IMAGE_WIDTH, FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, 1, this.diffractionData.getWidth());
        this.getIfdListing().get(0).addEntry(FieldTags.IMAGE_HEIGHT, FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, 1, this.diffractionData.getHeight());
        this.getIfdListing().get(0).addEntry(FieldTags.BITS_PER_SAMPLE, FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, 1, bitsPerSample);
        this.getIfdListing().get(0).addEntry(FieldTags.COMPRESSION, FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, 1, 1);
        this.getIfdListing().get(0).addEntry(FieldTags.PHOTOMETRIC_INTERPRETATION, FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, 1, 1);
        this.getIfdListing().get(0).addEntry(FieldTags.STRIP_OFFSETS, FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, 1, 512);
        this.getIfdListing().get(0).addEntry(FieldTags.ROWS_PER_STRIP, FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, 1, this.diffractionData.getHeight());
        this.getIfdListing().get(0).addEntry(FieldTags.STRIP_BYTE_COUNTS, FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, 1, imageByteCount);
        this.getIfdListing().get(0).addEntry(FieldTags.X_RESOLUTION_OFFSET, FieldTypes.RATIONAL, 1, byteLength);
        this.getIfdListing().get(0).addEntry(FieldTags.Y_RESOLUTION_OFFSET, FieldTypes.RATIONAL, 1, byteLength + ResolutionAxis.BYTE_LENGTH);
        this.getIfdListing().get(0).addEntry(FieldTags.RESOLUTION_UNIT, FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, 1, 3);
        this.getIfdListing().get(0).addEntry(FieldTags.SAMPLE_FORMAT, FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, 1, sampleFormat);
    }

    private void generateNewFileHeader() {
        this.header.setByteOrder(ByteOrder.nativeOrder());
        this.header.setFileID((short) 42);
        this.header.setFirstIfdOffset(8);
    }

    private void initializeImageObject(DiffractionFrame data) {
        this.calibration = new CalibrationData();
        this.diffractionData = data;
        this.fileOutputFormat = FileTypes.TIFF_32_BIT_INT;
    }

    private void syncImageResolutionData() {
        this.imageXResolution = this.diffractionData.getImageXResolution();
        this.imageYResolution = this.diffractionData.getImageYResolution();
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order) {
        super.fromByteArray(dataBytes, order);
        if (this.getIfdListing().get(0).getTagValue(FieldTags.CALIBRATION_DATA_OFFSET_SIGNED) != -1)
            this.getCalibrationData(dataBytes, this.header.getByteOrder());
        this.getImageData(dataBytes, this.header.getByteOrder());
    }

    @Override
    public byte[] toByteArray(ByteOrder order) {
        ByteBuffer bytes;
        byte[] emptyBytes, imageDataBytes, imageMetaBytes;
        int totalSize;

        this.generateNewFileHeader();
        this.syncImageResolutionData();
        imageDataBytes = this.createImageBytes(order, this.fileOutputFormat);
        this.generateNewIfdForImageExport(this.fileOutputFormat, imageDataBytes.length);

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
