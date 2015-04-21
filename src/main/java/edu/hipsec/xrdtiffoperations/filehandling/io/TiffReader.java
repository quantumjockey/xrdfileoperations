package edu.hipsec.xrdtiffoperations.filehandling.io;

import edu.hipsec.xrdtiffoperations.file.mardetector.martiff.MARTiffImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TiffReader {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    // File Data
    private byte[] fileBytesRaw;

    // Image data
    private MARTiffImage marImageData;
    private boolean fileHasBeenRead;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffReader(Path filePath) throws IOException {
        this.fileBytesRaw = Files.readAllBytes(filePath);
        this.marImageData = new MARTiffImage(filePath.getFileName().toString());
        this.fileHasBeenRead = false;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void readFileData() {
        this.marImageData.fromByteArray(this.fileBytesRaw, null);
        this.fileHasBeenRead = true;
    }

    public MARTiffImage getImageData() {
        if (this.fileHasBeenRead)
            return this.marImageData;
        else
            return null;
    }

}
