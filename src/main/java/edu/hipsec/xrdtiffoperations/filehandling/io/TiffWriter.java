package edu.hipsec.xrdtiffoperations.filehandling.io;

import edu.hipsec.xrdtiffoperations.datamodel.mardetector.martiff.MARTiffImage;
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

    public TiffWriter(MARTiffImage dataSource, String fileType) {
        this.cachedData = dataSource;
        this.cachedData.setFileOutputFormat(fileType);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void write(String path) {
        byte[] allBytes;
        Path destination;

        allBytes = this.generateFileBytes();
        destination = Paths.get(path);

        try {
            Files.write(destination, allBytes);
        } catch (IOException e) {
            System.out.println("File could not be written.");
        }
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private byte[] generateFileBytes() {
        ByteOrder order;
        int byteCount;
        byte[] imageData;
        ByteBuffer bytes;

        order = this.cachedData.getHeader().getByteOrder();
        imageData = this.cachedData.toByteArray(order);
        byteCount = imageData.length;
        bytes = ByteBuffer.allocate(byteCount);
        bytes.put(imageData);

        return bytes.array();
    }

}
