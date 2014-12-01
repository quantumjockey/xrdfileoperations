package xrdtiffoperations.filehandling.io;

import xrdtiffoperations.filehandling.bytewrappers.TiffByteOrderWrapper;
import xrdtiffoperations.filehandling.bytewrappers.UnsignedShortWrapper;
import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.ifd.fields.FieldTags;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.filehandling.bytewrappers.SignedIntWrapper;
import xrdtiffoperations.filehandling.bytewrappers.SignedShortWrapper;
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
        int lastIfdByte;

        getFileHeader(fileBytesRaw);
        lastIfdByte = getIFDByteGroups(fileBytesRaw, marImageData.getFirstIfdOffset());
        getCalibrationData(lastIfdByte, fileBytesRaw);
        retrieveImageData(retrieveImageStartingByte(), retrieveImageHeight(), retrieveImageWidth());
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

    private void getCalibrationData(int ifdEndByte, byte[] fileBytes){
        byte[] bytes;

        bytes = getCalibrationDataBytes(ifdEndByte, fileBytes);
        marImageData.setCalibration(new CalibrationData(
                ifdEndByte,
                marImageData.searchDirectoriesForTag(FieldTags.X_RESOLUTION_OFFSET),
                marImageData.searchDirectoriesForTag(FieldTags.Y_RESOLUTION_OFFSET),
                marImageData.searchDirectoriesForTag(FieldTags.CALIBRATION_DATA_OFFSET),
                bytes,
                marImageData.getByteOrder()
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
        TiffByteOrderWrapper _byteOrder;
        SignedShortWrapper _identifier;
        SignedIntWrapper _ifdOffset;

        _byteOrder = new TiffByteOrderWrapper();

        // extract byte order
        System.arraycopy(imageData, 0, _byteOrder.getDataBytes(), 0, 2);
        marImageData.setByteOrder(_byteOrder.get());

        _identifier = new SignedShortWrapper(_byteOrder.get());
        _ifdOffset = new SignedIntWrapper(_byteOrder.get());

        // extract file identifier
        System.arraycopy(imageData, 2, _identifier.getDataBytes(), 0, 2);
        marImageData.setIdentifier(_identifier.get());

        // extract first IFD offset value
        System.arraycopy(imageData, 4, _ifdOffset.getDataBytes(), 0, 4);
        marImageData.setFirstIfdOffset(_ifdOffset.get());
    }

    private int getIFDByteGroups(byte[] imageData, int firstIfdOffset){
        ByteOrder _byteOrder;
        SignedShortWrapper _fieldsCount;
        byte[] directoryBytes;
        int directoryLength, fieldsCount;
        ImageFileDirectory directory;

        _byteOrder = marImageData.getByteOrder();
        _fieldsCount = new SignedShortWrapper(_byteOrder);

        // extract fields count
        System.arraycopy(imageData, firstIfdOffset, _fieldsCount.getDataBytes(), 0, 2);

        fieldsCount = _fieldsCount.get();
        directoryLength = 2 + (fieldsCount * 12) + 4;
        directoryBytes = new byte[directoryLength];

        // extract remaining IFD data
        System.arraycopy(imageData, firstIfdOffset, directoryBytes, 0, directoryLength);

        directory = new ImageFileDirectory(directoryBytes, _byteOrder);
        marImageData.getIfdListing().add(directory);

        return firstIfdOffset + directoryLength;
    }

    private void retrieveImageData(int startingByte, int imageHeight, int imageWidth){
        ByteOrder _byteOrder;
        int[] linearImageArray;
        UnsignedShortWrapper pixelTemp;
        int z;

        _byteOrder = marImageData.getByteOrder();
        linearImageArray = new int[imageHeight * imageWidth];
        pixelTemp = new UnsignedShortWrapper(_byteOrder);
        z = 0;

        for(int i = 0; i < ((fileBytesRaw.length + FIT2D_STARTING_BYTE_SHIFT) - startingByte); i++){
            if ((startingByte + i ) % 2 == 0){
                pixelTemp.getDataBytes()[0] = fileBytesRaw[startingByte + i];
            }
            else if ((startingByte + i ) % 2 != 0) {
                pixelTemp.getDataBytes()[1] = fileBytesRaw[startingByte + i];
                linearImageArray[z] = pixelTemp.get();
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

    private int retrieveImageHeight(){
        return marImageData.searchDirectoriesForTag(FieldTags.IMAGE_HEIGHT);
    }

    private int retrieveImageWidth(){
        return marImageData.searchDirectoriesForTag(FieldTags.IMAGE_WIDTH);
    }

}
