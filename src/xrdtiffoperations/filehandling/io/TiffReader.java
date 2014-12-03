package xrdtiffoperations.filehandling.io;

import xrdtiffoperations.filehandling.bytewrappers.UnsignedShortWrapper;
import xrdtiffoperations.imagemodel.header.TiffHeader;
import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.ifd.fields.FieldTags;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.imagemodel.martiff.WritableMARTiffImage;
import xrdtiffoperations.imagemodel.martiff.components.CalibrationData;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

public class TiffReader {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int FIT2D_STARTING_BYTE_SHIFT = -2;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    // File Data
    private byte[] fileBytesRaw;

    // Image data
    private WritableMARTiffImage marImageData;
    private boolean fileHasBeenRead;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffReader(Path filePath) throws IOException{
        fileBytesRaw = Files.readAllBytes(filePath);
        marImageData = new WritableMARTiffImage(filePath.getFileName().toString());
        fileHasBeenRead = false;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void readFileData(){
        getFileHeader(fileBytesRaw);
        getFirstIFD(fileBytesRaw, marImageData.getHeader().getFirstIfdOffset(), marImageData.getHeader().getByteOrder());
        getCalibrationData(fileBytesRaw, marImageData.getHeader().getByteOrder());
        getImageData(retrieveImageStartingByte(), retrieveImageHeight(), retrieveImageWidth(), marImageData.getHeader().getByteOrder());
        fileHasBeenRead = true;
    }

    public MARTiffImage getImageData(){
        if (fileHasBeenRead) {
            return marImageData;
        }
        else {
            return null;
        }
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void getCalibrationData(byte[] fileBytes, ByteOrder _byteOrder){
        byte[] bytes;
        int ifdEndByte;

        ifdEndByte = marImageData.getHeader().getFirstIfdOffset() + marImageData.getIfdListing().get(0).getByteLength();
        bytes = getCalibrationDataBytes(ifdEndByte, fileBytes);
        marImageData.setCalibration(new CalibrationData(
                ifdEndByte,
                marImageData.searchDirectoriesForTag(FieldTags.X_RESOLUTION_OFFSET),
                marImageData.searchDirectoriesForTag(FieldTags.Y_RESOLUTION_OFFSET),
                marImageData.searchDirectoriesForTag(FieldTags.CALIBRATION_DATA_OFFSET),
                bytes,
                _byteOrder
        ));
    }

    private byte[] getCalibrationDataBytes(int ifdEndByte, byte[] fileBytes){
        int imageStartByte, bufferLength;
        byte[] data;

        imageStartByte = marImageData.searchDirectoriesForTag(FieldTags.STRIP_OFFSETS);
        bufferLength = imageStartByte - ifdEndByte;
        data = new byte[bufferLength];
        System.arraycopy(fileBytes, ifdEndByte, data, 0, bufferLength);

        return data;
    }

    private void getFileHeader(byte[] imageData){
        byte[] headerBytes = new byte[TiffHeader.BYTE_LENGTH];
        System.arraycopy(imageData, 0, headerBytes, 0, TiffHeader.BYTE_LENGTH);
        marImageData.getHeader().fromByteArray(headerBytes, null);
    }

    private int getFirstIFD(byte[] imageData, int firstIfdOffset, ByteOrder _byteOrder){
        byte[] directoryBytes;
        int directoryLength, fieldsCount;
        ImageFileDirectory directory;

        fieldsCount = ImageFileDirectory.extractNumFields(imageData, firstIfdOffset, _byteOrder);
        directoryLength = ImageFileDirectory.calculateDirectoryLengthWithoutFieldsCount(fieldsCount);
        directoryBytes = new byte[directoryLength];

        // extract remaining IFD data
        System.arraycopy(imageData, firstIfdOffset + ImageFileDirectory.FIELD_COUNT_LENGTH, directoryBytes, 0, directoryLength);

        directory = new ImageFileDirectory();
        directory.fromByteArray(directoryBytes, _byteOrder);
        marImageData.getIfdListing().add(directory);

        return firstIfdOffset + directoryLength;
    }

    private void getImageData(int startingByte, int imageHeight, int imageWidth, ByteOrder _byteOrder){
        int[] linearImageArray;
        UnsignedShortWrapper pixelTemp;
        int z;

        linearImageArray = new int[imageHeight * imageWidth];
        pixelTemp = new UnsignedShortWrapper(_byteOrder);
        z = 0;

        for(int i = 0; i < (retrieveImageDataLength()); i++){
            if ((startingByte + i ) % 2 == 0){
                pixelTemp.getDataBytes()[0] = fileBytesRaw[startingByte + i];
            }
            else if ((startingByte + i ) % 2 != 0) {
                pixelTemp.getDataBytes()[1] = fileBytesRaw[startingByte + i];
                linearImageArray[z] = pixelTemp.getAsInt();
                z++;
            }
        }
        marImageData.initializeIntensityMap(imageHeight, imageWidth);
        for (int y = 0; y < imageHeight; y++){
            for (int x = 0; x < imageWidth; x++){
                marImageData.setIntensityMapCoordinate(y, x, linearImageArray[x + (y * imageHeight)]);
            }
        }
    }

    private int retrieveImageStartingByte(){
        return marImageData.searchDirectoriesForTag(FieldTags.STRIP_OFFSETS) + FIT2D_STARTING_BYTE_SHIFT;
    }

    private int retrieveImageDataLength(){
        return marImageData.searchDirectoriesForTag(FieldTags.STRIP_BYTE_COUNTS);
    }

    private int retrieveImageHeight(){
        return marImageData.searchDirectoriesForTag(FieldTags.IMAGE_HEIGHT);
    }

    private int retrieveImageWidth(){
        return marImageData.searchDirectoriesForTag(FieldTags.IMAGE_WIDTH);
    }

}
