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
import xrdtiffoperations.imagemodel.ifd.fields.FieldTags;
import xrdtiffoperations.imagemodel.ifd.fields.FieldTypes;
import xrdtiffoperations.imagemodel.ifd.fields.SampleTypes;
import xrdtiffoperations.imagemodel.martiff.components.CalibrationData;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MARTiffImage extends TiffBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected DiffractionFrame generatedImage;

    // For indicating file format when converting data to byte array
    private String fileOutputFormat;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public DiffractionFrame getGeneratedImage(){
        return generatedImage;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffImage(String _filename) {
        super(_filename);
        generatedImage = new DiffractionFrame();
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

        numPixels = generatedImage.getHeight() * generatedImage.getWidth();

        switch (imageType) {
            case FileTypes.TIFF_32_BIT_FLOAT:
                bytes = createByteBuffer(order, numPixels, 4);
                generatedImage.cycleImageDataBytes((y, x) -> bytes.putFloat((float) generatedImage.getIntensityMapValue(y, x)));
                break;
            default: //FileTypes.TIFF_32_BIT_INT:
                bytes = createByteBuffer(order, numPixels, 4);
                generatedImage.cycleImageDataBytes((y, x) -> bytes.putInt(generatedImage.getIntensityMapValue(y, x)));
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

        generatedImage.getCalibration().fromByteArray(data, _byteOrder);
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

        generatedImage.initializeIntensityMap(imageHeight, imageWidth);

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

        generatedImage.cycleImageDataBytes((y, x) -> {
            System.arraycopy(fileBytes, startingByte + ((x + (y * imageHeight)) * sampleByteLength), pixelTemp.getDataBytes(), 0, sampleByteLength);
            generatedImage.setIntensityMapCoordinate(y, x, pixelTemp.getAsIntPrimitive());
        });
    }

    private void modifyImageIfdForImageExport(String imageType, int imageByteCount){
        int bitsPerSample, sampleFormat;

        this.getIfdListing().get(0).removeEntry(FieldTags.BITS_PER_SAMPLE);
        this.getIfdListing().get(0).removeEntry(FieldTags.STRIP_OFFSETS);
        this.getIfdListing().get(0).removeEntry(FieldTags.ORIENTATION);
        this.getIfdListing().get(0).removeEntry(FieldTags.CALIBRATION_DATA_OFFSET_SIGNED);
        this.getIfdListing().get(0).removeEntry(FieldTags.STRIP_BYTE_COUNTS);
        this.getIfdListing().get(0).removeEntry(FieldTags.SAMPLE_FORMAT);

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

        this.getIfdListing().get(0).addEntry(FieldTags.BITS_PER_SAMPLE, FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, 1, bitsPerSample);
        this.getIfdListing().get(0).addEntry(FieldTags.STRIP_OFFSETS, FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, 1, 512);
        this.getIfdListing().get(0).addEntry(FieldTags.STRIP_BYTE_COUNTS, FieldTypes.THIRTY_TWO_BIT_UNSIGNED_INT, 1, imageByteCount);
        this.getIfdListing().get(0).addEntry(FieldTags.SAMPLE_FORMAT, FieldTypes.SIXTEEN_BIT_UNSIGNED_INT, 1, sampleFormat);

        int byteLength = TiffHeader.BYTE_LENGTH + this.getIfdListing().get(0).getByteLength();

        this.getIfdListing().get(0).removeEntry(FieldTags.X_RESOLUTION_OFFSET);
        this.getIfdListing().get(0).addEntry(FieldTags.X_RESOLUTION_OFFSET, FieldTypes.RATIONAL, 1, byteLength);

        this.getIfdListing().get(0).removeEntry(FieldTags.Y_RESOLUTION_OFFSET);
        this.getIfdListing().get(0).addEntry(FieldTags.Y_RESOLUTION_OFFSET, FieldTypes.RATIONAL, 1, byteLength + ResolutionAxis.BYTE_LENGTH);

        this.getIfdListing().get(0).sort();
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
        modifyImageIfdForImageExport(this.fileOutputFormat, imageDataBytes.length);

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
