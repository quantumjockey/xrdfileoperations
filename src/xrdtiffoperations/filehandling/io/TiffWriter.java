package xrdtiffoperations.filehandling.io;

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

    private byte[] generateFileBytes(){
        ByteOrder order;
        int byteCount;
        byte[] imageData;
        ByteBuffer bytes;

        order = cachedData.getHeader().getByteOrder();
        imageData = cachedData.toByteArray(order);
        byteCount = imageData.length;
        bytes = ByteBuffer.allocate(byteCount);
        bytes.put(imageData);

        return bytes.array();
    }

}
