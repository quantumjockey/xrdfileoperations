package edu.hipsec.xrdtiffoperations.file.tiff.ifd.fields;

import edu.hipsec.xrdtiffoperations.bytewrappers.SignedShortWrapper;
import edu.hipsec.xrdtiffoperations.utilities.bytes.ByteSerializer;
import edu.hipsec.xrdtiffoperations.bytewrappers.SignedIntWrapper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DirectoryField extends ByteSerializer {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private static final int COUNT_BYTES_LENGTH = 4;
    private static final int TAG_BYTES_LENGTH = 2;
    private static final int TYPE_BYTES_LENGTH = 2;
    private static final int VALUE_BYTES_LENGTH = 4;

    public static final int BYTE_LENGTH = TAG_BYTES_LENGTH + TYPE_BYTES_LENGTH + COUNT_BYTES_LENGTH + VALUE_BYTES_LENGTH;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected short tag;
    protected short type;
    protected int count;
    protected int value;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public short getTag() {
        return this.tag;
    }

    public short getType() {
        return this.type;
    }

    public int getCount() {
        return this.count;
    }

    public int getValue() {
        return this.value;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DirectoryField() {
        this.count = 1;
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public boolean fromByteArray(byte[] dataBytes, ByteOrder order) {
        SignedShortWrapper fieldTag, fieldType;
        SignedIntWrapper fieldValue, typeCount;

        int numZeroes = 0;
        for(byte element: dataBytes) {
            if (element == (byte)0)
                numZeroes++;
        }

        if (numZeroes == BYTE_LENGTH)
            return false;

        if (dataBytes.length >= BYTE_LENGTH) {
            fieldTag = new SignedShortWrapper(order);
            fieldType = new SignedShortWrapper(order);
            typeCount = new SignedIntWrapper(order);
            fieldValue = new SignedIntWrapper(order);

            fieldTag.extractFromSourceArray(dataBytes, 0);
            fieldType.extractFromSourceArray(dataBytes, TAG_BYTES_LENGTH);
            typeCount.extractFromSourceArray(dataBytes, TAG_BYTES_LENGTH + TYPE_BYTES_LENGTH);
            fieldValue.extractFromSourceArray(dataBytes, TAG_BYTES_LENGTH + TYPE_BYTES_LENGTH + COUNT_BYTES_LENGTH);

            this.tag = fieldTag.get();
            this.type = fieldType.get();
            this.count = typeCount.get();
            this.value = fieldValue.get();
            return true;
        } else {
            this.displaySizeAlert(dataBytes.length, BYTE_LENGTH);
            return false;
        }
    }

    @Override
    public byte[] toByteArray(ByteOrder order) {
        ByteBuffer bytes;
        bytes = ByteBuffer.allocate(BYTE_LENGTH);
        bytes.order(order);
        bytes.putShort(this.getTag());
        bytes.putShort(this.getType());
        bytes.putInt(this.getCount());
        bytes.putInt(this.getValue());
        return bytes.array();
    }

}
