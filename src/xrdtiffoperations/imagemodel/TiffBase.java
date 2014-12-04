package xrdtiffoperations.imagemodel;

import xrdtiffoperations.imagemodel.header.TiffHeader;
import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.ifd.fields.DirectoryField;
import xrdtiffoperations.imagemodel.ifd.fields.FieldTags;
import xrdtiffoperations.imagemodel.attributes.ResolutionAxis;
import xrdtiffoperations.imagemodel.serialization.ByteSerializer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class TiffBase extends ByteSerializer {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected String filename;
    protected TiffHeader header;
    protected ArrayList<ImageFileDirectory> ifdListing;
    protected ResolutionAxis imageXResolution;
    protected ResolutionAxis imageYResolution;

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

    public ResolutionAxis getImageXResolution(){
        return imageXResolution;
    }

    public ResolutionAxis getImageYResolution(){
        return imageYResolution;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffBase(String _filename) {
        ifdListing = new ArrayList<>();
        filename = _filename;
        header = new TiffHeader();
        imageXResolution = new ResolutionAxis();
        imageYResolution = new ResolutionAxis();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public int searchDirectoriesForTag(int tag){
        int _value = 0;
        for (ImageFileDirectory directory : ifdListing){
            for (DirectoryField item : directory.getFields()){
                if (item.getTag() == tag){
                    _value = item.getValue();
                }
            }
        }
        return _value;
    }

    /////////// Private Methods //////////////////////////////////////////////////////////////

    private void getFileHeader(byte[] imageData){
        byte[] headerBytes = new byte[TiffHeader.BYTE_LENGTH];
        System.arraycopy(imageData, 0, headerBytes, 0, TiffHeader.BYTE_LENGTH);
        header.fromByteArray(headerBytes, null);
    }

    private void getFirstIFD(byte[] imageData, int firstIfdOffset, ByteOrder _byteOrder){
        byte[] directoryBytes;
        int directoryLength, fieldsCount;
        ImageFileDirectory directory;

        fieldsCount = ImageFileDirectory.extractNumFields(imageData, firstIfdOffset, _byteOrder);
        directoryLength = ImageFileDirectory.calculateDirectoryLengthWithoutFieldsCount(fieldsCount);
        directoryBytes = new byte[directoryLength];

        // extract remaining IFD data
        System.arraycopy(imageData, firstIfdOffset + ImageFileDirectory.FIELD_COUNT_LENGTH, directoryBytes, 0, directoryLength);

        directory = new ImageFileDirectory();
        directory.fromByteArray(directoryBytes, _byteOrder);
        ifdListing.add(directory);
    }

    private void getImageResolution(byte[] imageData, ByteOrder order){
        byte[] xRes, yRes;

        xRes = new byte[ResolutionAxis.BYTE_LENGTH];
        System.arraycopy(imageData, searchDirectoriesForTag(FieldTags.X_RESOLUTION_OFFSET), xRes, 0, ResolutionAxis.BYTE_LENGTH);
        imageXResolution.fromByteArray(xRes, order);

        yRes = new byte[ResolutionAxis.BYTE_LENGTH];
        System.arraycopy(imageData, searchDirectoriesForTag(FieldTags.Y_RESOLUTION_OFFSET), yRes, 0, ResolutionAxis.BYTE_LENGTH);
        imageYResolution.fromByteArray(yRes, order);
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order){
        getFileHeader(dataBytes);
        getFirstIFD(dataBytes, header.getFirstIfdOffset(), header.getByteOrder());
        getImageResolution(dataBytes, order);
    }

    @Override
    public byte[] toByteArray(ByteOrder order){
        ByteBuffer bytes;

        bytes = ByteBuffer.allocate(TiffHeader.BYTE_LENGTH + ifdListing.get(0).getByteLength() + (ResolutionAxis.BYTE_LENGTH * 2));
        bytes.order(order);
        bytes.put(header.toByteArray(order));
        bytes.put(ifdListing.get(0).toByteArray(order));
        bytes.put(imageXResolution.toByteArray(order));
        bytes.put(imageYResolution.toByteArray(order));

        return bytes.array();
    }

}
