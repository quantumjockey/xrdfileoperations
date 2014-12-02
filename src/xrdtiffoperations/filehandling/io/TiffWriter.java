package xrdtiffoperations.filehandling.io;

import xrdtiffoperations.imagemodel.ifd.fields.FieldTags;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TiffWriter {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int HEADER_LENGTH = 8;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private MARTiffImage cachedData;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffWriter(MARTiffImage dataSource){
        cachedData = dataSource;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void write(String path){
        byte[] allBytes;
        Path destination;

        allBytes = generateFileBytes();
        destination = Paths.get(path);

        try {
            Files.write(destination, allBytes);
        }
        catch (IOException e){
            System.out.println("File could not be written.");
        }
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private byte[] createHeaderBytes(ByteOrder order){
        ByteBuffer bytes;
        String id;

        bytes = ByteBuffer.allocate(HEADER_LENGTH);
        bytes.order(order);

        if(order == ByteOrder.BIG_ENDIAN){
            id = "MM";
        }
        else{
            id = "II";
        }
        bytes.put(id.getBytes());
        bytes.putShort((short)42);
        bytes.putInt(HEADER_LENGTH);

        return bytes.array();
    }

    private byte[] createImageBytes(ByteOrder order){
        int gridHeight, gridWidth, numBytes;
        ByteBuffer bytes;

        numBytes = cachedData.getIfdListing().get(0).getTagValue(FieldTags.STRIP_BYTE_COUNTS);
        bytes = ByteBuffer.allocate(numBytes);
        bytes.order(order);
        gridHeight = cachedData.getHeight();
        gridWidth = cachedData.getWidth();

        for (int y = 0; y < gridHeight; y++){
            for (int x = 0; x < gridWidth; x++){
                bytes.putShort((short)cachedData.getIntensityMapValue(y, x));
                // risky cast - must determine methods to explicitly prevent buffer overflow without marring data integrity
            }
        }

        return bytes.array();
    }

    private byte[] createRegionBeforeImageData(ByteOrder order, int lengthOfHeaderPlusIFD) {
        int imageOffset, emptyRegion;
        int regionLength;
        ByteBuffer bytes;
        byte[] emptyBytes;

        imageOffset = cachedData.getIfdListing().get(0).getTagValue(FieldTags.STRIP_OFFSETS);
        regionLength = imageOffset - lengthOfHeaderPlusIFD;
        bytes = ByteBuffer.allocate(regionLength);
        bytes.order(order);
        bytes.putInt(cachedData.getCalibration().getDetectorXResolution().getNumerator());
        bytes.putInt(cachedData.getCalibration().getDetectorXResolution().getDenominator());
        bytes.putInt(cachedData.getCalibration().getDetectorYResolution().getNumerator());
        bytes.putInt(cachedData.getCalibration().getDetectorYResolution().getDenominator());
        emptyRegion = regionLength - (cachedData.getCalibration().getCoreCalibrationBytes().length + 16);
        emptyBytes = new byte[emptyRegion];
        for (int i = 0; i < emptyRegion; i++){
            emptyBytes[i] = 0;
        }
        bytes.put(emptyBytes);
        bytes.put(cachedData.getCalibration().getCoreCalibrationBytes());

        return bytes.array();
    }

    private byte[] generateFileBytes(){
        ByteOrder order;
        int byteCount, lengthOfHeadPlusIfd;
        byte[] header, ifd, image, region;
        ByteBuffer bytes;

        order = cachedData.getByteOrder();
        header = createHeaderBytes(order);
        ifd = cachedData.getIfdListing().get(0).toByteArray(order);
        lengthOfHeadPlusIfd = header.length + ifd.length;
        region = createRegionBeforeImageData(order, lengthOfHeadPlusIfd);
        image = createImageBytes(order);
        byteCount = header.length + ifd.length + region.length + image.length;
        bytes = ByteBuffer.allocate(byteCount);
        bytes.put(header);
        bytes.put(ifd);
        bytes.put(region);
        bytes.put(image);

        return bytes.array();
    }

}
