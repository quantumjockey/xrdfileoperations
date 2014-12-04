package xrdtiffoperations.imagemodel;

import xrdtiffoperations.imagemodel.header.TiffHeader;
import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import xrdtiffoperations.imagemodel.serialization.ByteSerializer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class TiffBase extends ByteSerializer {

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

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order){
        getFileHeader(dataBytes);
        getFirstIFD(dataBytes, header.getFirstIfdOffset(), header.getByteOrder());
    }

    @Override
    public byte[] toByteArray(ByteOrder order){
        ByteBuffer bytes;

        bytes = ByteBuffer.allocate(TiffHeader.BYTE_LENGTH + ifdListing.get(0).getByteLength());
        bytes.order(order);
        bytes.put(header.toByteArray(order));
        bytes.put(ifdListing.get(0).toByteArray(order));

        return bytes.array();
    }

}
