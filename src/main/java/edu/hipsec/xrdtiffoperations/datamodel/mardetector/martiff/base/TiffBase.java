package edu.hipsec.xrdtiffoperations.datamodel.mardetector.martiff.base;

import edu.hipsec.xrdtiffoperations.datamodel.mardetector.martiff.imagemodel.attributes.ResolutionAxis;
import edu.hipsec.xrdtiffoperations.datamodel.mardetector.martiff.imagemodel.ifd.ImageFileDirectory;
import edu.hipsec.xrdtiffoperations.datamodel.serialization.ByteSerializer;
import edu.hipsec.xrdtiffoperations.datamodel.mardetector.martiff.imagemodel.header.TiffHeader;
import edu.hipsec.xrdtiffoperations.datamodel.mardetector.martiff.imagemodel.ifd.fields.DirectoryField;
import edu.hipsec.xrdtiffoperations.datamodel.mardetector.martiff.imagemodel.ifd.fields.FieldTags;

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

    public String getFilename() {
        return this.filename;
    }

    public TiffHeader getHeader() {
        return this.header;
    }

    public ArrayList<ImageFileDirectory> getIfdListing() {
        return this.ifdListing;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TiffBase(String _filename) {
        this.ifdListing = new ArrayList<>();
        this.filename = _filename;
        this.header = new TiffHeader();
        this.imageXResolution = new ResolutionAxis();
        this.imageYResolution = new ResolutionAxis();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public int searchDirectoriesForTag(int tag) {
        int _value = 0;
        for (ImageFileDirectory directory : this.ifdListing)
            for (DirectoryField item : directory.getFields())
                if (item.getTag() == tag)
                    _value = item.getValue();
        return _value;
    }

    /////////// Private Methods //////////////////////////////////////////////////////////////

    private void getFileHeader(byte[] imageData) {
        byte[] headerBytes = new byte[TiffHeader.BYTE_LENGTH];
        System.arraycopy(imageData, 0, headerBytes, 0, TiffHeader.BYTE_LENGTH);
        this.header.fromByteArray(headerBytes, null);
    }

    private void getFirstIFD(byte[] imageData, int firstIfdOffset, ByteOrder _byteOrder) {
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
        this.ifdListing.add(directory);
    }

    private void getImageResolution(byte[] imageData, ByteOrder order) {
        byte[] xRes, yRes;

        xRes = new byte[ResolutionAxis.BYTE_LENGTH];
        System.arraycopy(imageData, this.searchDirectoriesForTag(FieldTags.X_RESOLUTION_OFFSET), xRes, 0, ResolutionAxis.BYTE_LENGTH);
        this.imageXResolution.fromByteArray(xRes, order);

        yRes = new byte[ResolutionAxis.BYTE_LENGTH];
        System.arraycopy(imageData, this.searchDirectoriesForTag(FieldTags.Y_RESOLUTION_OFFSET), yRes, 0, ResolutionAxis.BYTE_LENGTH);
        this.imageYResolution.fromByteArray(yRes, order);
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order) {
        this.getFileHeader(dataBytes);
        this.getFirstIFD(dataBytes, this.header.getFirstIfdOffset(), this.header.getByteOrder());
        this.getImageResolution(dataBytes, order);
    }

    @Override
    public byte[] toByteArray(ByteOrder order) {
        ByteBuffer bytes;
        bytes = ByteBuffer.allocate(TiffHeader.BYTE_LENGTH + this.ifdListing.get(0).getByteLength() + (ResolutionAxis.BYTE_LENGTH * 2));
        bytes.order(order);
        bytes.put(this.header.toByteArray(order));
        bytes.put(this.ifdListing.get(0).toByteArray(order));
        bytes.put(this.imageXResolution.toByteArray(order));
        bytes.put(this.imageYResolution.toByteArray(order));
        return bytes.array();
    }

}
