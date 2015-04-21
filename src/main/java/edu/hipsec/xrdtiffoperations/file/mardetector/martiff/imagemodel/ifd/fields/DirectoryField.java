package edu.hipsec.xrdtiffoperations.file.mardetector.martiff.imagemodel.ifd.fields;

import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.SignedShortWrapper;
import edu.hipsec.xrdtiffoperations.file.serialization.ByteSerializer;
import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.SignedIntWrapper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DirectoryField extends ByteSerializer {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int COUNT_BYTES_LENGTH = 4;
    private final int TAG_BYTES_LENGTH = 2;
    private final int TYPE_BYTES_LENGTH = 2;
    private final int VALUE_BYTES_LENGTH = 4;

    public static final int BYTE_LENGTH = 12;

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
    public void fromByteArray(byte[] dataBytes, ByteOrder order) {
        SignedShortWrapper _fieldTag, _fieldType;
        SignedIntWrapper _fieldValue, _typeCount;

        if (dataBytes.length == BYTE_LENGTH) {
            _fieldTag = new SignedShortWrapper(order);
            _fieldType = new SignedShortWrapper(order);
            _typeCount = new SignedIntWrapper(order);
            _fieldValue = new SignedIntWrapper(order);

            System.arraycopy(dataBytes, 0, _fieldTag.getDataBytes(), 0, this.TAG_BYTES_LENGTH);
            System.arraycopy(dataBytes, this.TAG_BYTES_LENGTH, _fieldType.getDataBytes(), 0, this.TYPE_BYTES_LENGTH);
            System.arraycopy(dataBytes, this.TAG_BYTES_LENGTH + this.TYPE_BYTES_LENGTH, _typeCount.getDataBytes(), 0, this.COUNT_BYTES_LENGTH);
            System.arraycopy(dataBytes, this.TAG_BYTES_LENGTH + this.TYPE_BYTES_LENGTH + this.COUNT_BYTES_LENGTH, _fieldValue.getDataBytes(), 0, this.VALUE_BYTES_LENGTH);

            this.tag = _fieldTag.get();
            this.type = _fieldType.get();
            this.count = _typeCount.get();
            this.value = _fieldValue.get();
        } else
            this.displaySizeAlert(dataBytes.length, BYTE_LENGTH);
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
