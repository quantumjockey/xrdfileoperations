package xrdtiffoperations.filehandling.io;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.imagemodel.martiff.WritableMARTiffImage;
import java.io.IOException;
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
        marImageData.fromByteArray(fileBytesRaw, null);
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

}
