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

    public static final int ENTRY_LENGTH = 12;

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

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public FieldInformation(){
        this.count = 1;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] fieldData, ByteOrder order){
        SignedShortWrapper _fieldTag, _fieldType;
        SignedIntWrapper _fieldValue, _typeCount;

        if (fieldData.length == ENTRY_LENGTH) {
            _fieldTag = new SignedShortWrapper(order);
            _fieldType = new SignedShortWrapper(order);
            _typeCount = new SignedIntWrapper(order);
            _fieldValue = new SignedIntWrapper(order);

            for (int i = 0; i < ENTRY_LENGTH; i++) {
                if (i < 2) {
                    _fieldTag.getDataBytes()[i] = fieldData[i];
                } else if (i >= 2 && i < 4) {
                    _fieldType.getDataBytes()[i - 2] = fieldData[i];
                } else if (i >= 4 && i < 8) {
                    _typeCount.getDataBytes()[i - 4] = fieldData[i];
                } else if (i >= 8 && i < 12) {
                    _fieldValue.getDataBytes()[i - 8] = fieldData[i];
                }
            }

            tag = _fieldTag.get();
            type = _fieldType.get();
            count = _typeCount.get();
            value = _fieldValue.get();
        }
        else{
            displaySizeAlert(fieldData.length, ENTRY_LENGTH);
        }
    }

    @Override
    public byte[] toByteArray(ByteOrder order){
        ByteBuffer bytes;

        bytes = ByteBuffer.allocate(ENTRY_LENGTH);
        bytes.order(order);
        bytes.putShort(this.getTag());
        bytes.putShort(this.getType());
        bytes.putInt(this.getCount());
        bytes.putInt(this.getValue());

        return bytes.array();
    }

}
