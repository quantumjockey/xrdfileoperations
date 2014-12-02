package xrdtiffoperations.imagemodel;

import xrdtiffoperations.imagemodel.header.TiffHeader;
import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class TiffBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected String filename;
    protected TiffHeader header;
    protected ArrayList<ImageFileDirectory> ifdListing;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public String getFilename(){
        return filename;
    }

    public TiffHeader getHeader(){
        return header;
    }

    public ArrayList<ImageFileDirectory> getIfdListing(){
        return ifdListing;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffBase(String _filename) {
        ifdListing = new ArrayList<>();
        filename = _filename;
        header = new TiffHeader();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public int searchDirectoriesForTag(int tag){
        int _value = 0;
        for (ImageFileDirectory directory : ifdListing){
            for (FieldInformation item : directory.getFields()){
                if (item.getTag() == tag){
                    _value = item.getValue();
                }
            }
        }
        return _value;
    }
}
