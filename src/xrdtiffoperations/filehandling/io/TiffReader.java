package xrdtiffoperations.filehandling.io;

import xrdtiffoperations.filehandling.bytewrappers.CharWrapper;
import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import xrdtiffoperations.imagemodel.ifd.fields.FieldTags;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.filehandling.bytewrappers.IntWrapper;
import xrdtiffoperations.filehandling.bytewrappers.ShortWrapper;
import xrdtiffoperations.imagemodel.martiff.WritableMARTiffImage;
import xrdtiffoperations.imagemodel.martiff.components.CalibrationData;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

public class TiffReader {

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

    private ByteOrder getByteOrder(String orderId){
        ByteOrder order;

        switch (orderId){
            case "II":
                order = ByteOrder.LITTLE_ENDIAN;
                break;
            case "MM":
                order = ByteOrder.BIG_ENDIAN;
                break;
            default:
                order = ByteOrder.nativeOrder();
        }

        return order;
    }

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
        byte[] _byteOrder,_identifier,_ifdOffset;
        int cursor;

        _byteOrder = new byte[2];
        _identifier = new byte[2];
        _ifdOffset = new byte[4];
        cursor = 0;

        while (cursor < 8) {
            if (cursor < 2){
                _byteOrder[cursor] = imageData[cursor];
            } else if(cursor >= 2 && cursor < 4){
                _identifier[cursor - 2] = imageData[cursor];
            } else if(cursor >= 4 && cursor <= 8) {
                _ifdOffset[cursor - 4] = imageData[cursor];
            }
            cursor++;
        }

        marImageData.setByteOrder(getByteOrder(new String(_byteOrder)));
        marImageData.setIdentifier((new ShortWrapper(_identifier, marImageData.getByteOrder())).get());
        marImageData.setFirstIfdOffset((new IntWrapper(_ifdOffset, marImageData.getByteOrder())).get());
    }

    private int getIFDByteGroups(byte[] imageData, int firstIfdOffset){
        byte[] _fieldsCount, directoryBytes;
        int directoryLength, fieldsCount;
        ImageFileDirectory directory;

        _fieldsCount = new byte[2];
        System.arraycopy(imageData, firstIfdOffset, _fieldsCount, 0, 2);
        fieldsCount = (new ShortWrapper(_fieldsCount, marImageData.getByteOrder())).get();
        directoryLength = 2 + (fieldsCount * 12) + 4;
        directoryBytes = new byte[directoryLength];
        System.arraycopy(imageData, firstIfdOffset, directoryBytes, 0, directoryLength);
        directory = new ImageFileDirectory(directoryBytes, marImageData.getByteOrder());
        marImageData.getIfdListing().add(directory);

        return firstIfdOffset + directoryLength;
    }

    private void retrieveImageData(int startingByte, int imageHeight, int imageWidth){
        int[] linearImageArray;
        byte[] pixelTemp;
        int z;

        linearImageArray = new int[imageHeight * imageWidth];
        pixelTemp = new byte[2];
        z = 0;

        for(int i = 0; i < (fileBytesRaw.length - startingByte); i++){
            if ((startingByte + i ) % 2 == 0){
                pixelTemp[0] = fileBytesRaw[startingByte + i];
            }
            else if ((startingByte + i ) % 2 != 0) {
                pixelTemp[1] = fileBytesRaw[startingByte + i];
                linearImageArray[z] = (int)(new CharWrapper(pixelTemp, marImageData.getByteOrder())).get();
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
        return marImageData.searchDirectoriesForTag(FieldTags.STRIP_OFFSETS);
    }

    private int retrieveImageHeight(){
        return marImageData.searchDirectoriesForTag(FieldTags.IMAGE_HEIGHT);
    }

    private int retrieveImageWidth(){
        return marImageData.searchDirectoriesForTag(FieldTags.IMAGE_WIDTH);
    }

}
