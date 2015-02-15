package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.imagemodel.header.TiffHeader;
import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import java.util.ArrayList;

public class WritableMARTiffImage extends MARTiffImage{

    /////////// Mutators ///////////////////////////////////////////////////////////////////

    // TiffBase fields

    public void setFilename(String name){
        filename = name;
    }

    public void setHeader(TiffHeader _header){
        header = _header;
    }

    public void setIfdListing(ArrayList<ImageFileDirectory> listing){
        ifdListing = listing;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public WritableMARTiffImage(String _filename){
        super(_filename);
    }

}
