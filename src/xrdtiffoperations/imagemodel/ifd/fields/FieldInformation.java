package xrdtiffoperations.imagemodel.ifd.fields;

import xrdtiffoperations.filehandling.bytewrappers.SignedIntWrapper;
import xrdtiffoperations.filehandling.bytewrappers.SignedShortWrapper;
import xrdtiffoperations.imagemodel.serialization.ByteSerializer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FieldInformation extends ByteSerializer {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int COUNT_BYTES_LENGTH = 4;
    private final int TAG_BYTES_LENGTH = 2;
    private final int TYPE_BYTES_LENGTH = 2;
    private final int VALUE_BYTES_LENGTH = 4;

    public static final int BYTE_LENGTH = 12;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private short tag;
    private short type;
    private int count;
    private int value;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public short getTag(){
        return tag;
    }

    public short getType(){
        return type;
    }

    public int getCount(){
        return count;
    }

    public int getValue(){
        return value;
    }

    /////////// Mutators ////////////////////////////////////////////////////////////////////

    public void setValue(int _value){
        this.value = _value;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public FieldInformation(){
        this.count = 1;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order){
        SignedShortWrapper _fieldTag, _fieldType;
        SignedIntWrapper _fieldValue, _typeCount;

        if (dataBytes.length == BYTE_LENGTH) {
            _fieldTag = new SignedShortWrapper(order);
            _fieldType = new SignedShortWrapper(order);
            _typeCount = new SignedIntWrapper(order);
            _fieldValue = new SignedIntWrapper(order);

            System.arraycopy(dataBytes, 0, _fieldTag.getDataBytes(), 0, TAG_BYTES_LENGTH);
            System.arraycopy(dataBytes, 2, _fieldType.getDataBytes(), 0, TYPE_BYTES_LENGTH);
            System.arraycopy(dataBytes, 4, _typeCount.getDataBytes(), 0, COUNT_BYTES_LENGTH);
            System.arraycopy(dataBytes, 8, _fieldValue.getDataBytes(), 0, VALUE_BYTES_LENGTH);

            tag = _fieldTag.get();
            type = _fieldType.get();
            count = _typeCount.get();
            value = _fieldValue.get();
        }
        else{
            displaySizeAlert(dataBytes.length, BYTE_LENGTH);
        }
    }

    @Override
    public byte[] toByteArray(ByteOrder order){
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
