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

    private byte[] generateFileBytes(){
        ByteOrder order;
        int byteCount;
        byte[] imageMeta, image;
        ByteBuffer bytes;

        order = cachedData.getHeader().getByteOrder();
        imageMeta = cachedData.toByteArray(order);
        image = createImageBytes(order);
        byteCount = imageMeta.length + image.length;
        bytes = ByteBuffer.allocate(byteCount);
        bytes.put(imageMeta);
        bytes.put(image);

        return bytes.array();
    }

}
