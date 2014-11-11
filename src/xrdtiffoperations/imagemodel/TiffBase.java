package xrdtiffoperations.imagemodel;

import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class TiffBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected ByteOrder byteOrder;
    protected short identifier;
    protected String filename;
    protected int firstIfdOffset;
    protected ArrayList<ImageFileDirectory> ifdListing;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public ByteOrder getByteOrder(){
        return byteOrder;
    }

    public short getIdentifier(){
        return identifier;
    }

    public String getFilename(){
        return filename;
    }

    public ArrayList<ImageFileDirectory> getIfdListing(){
        return ifdListing;
    }

    public int getFirstIfdOffset(){
        return firstIfdOffset;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffBase(String _filename) {
        ifdListing = new ArrayList<>();
        filename = _filename;
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
