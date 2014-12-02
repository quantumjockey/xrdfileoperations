package xrdtiffoperations.imagemodel.ifd;

import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import xrdtiffoperations.filehandling.bytewrappers.SignedShortWrapper;
import xrdtiffoperations.imagemodel.serialization.ByteSerializer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ImageFileDirectory extends ByteSerializer {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private static int FIELD_ENTRY_LENGTH = 12;

    private final int IFD_BUFFER_LENGTH = 4;
    private final int IFD_ENTRY_COUNT_LENGTH = 2;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private int numFields;
    private ArrayList<FieldInformation> fields;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public ArrayList<FieldInformation> getFields(){
        return fields;
    }

    /////////// Constructors //////////////////////////////////////////////////////////////////

    public ImageFileDirectory(){
        fields = new ArrayList<>();
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public int getTagValue(short specifiedTag){
        int value;

        value = -1;
        for (FieldInformation item : fields){
            if (item.getTag() == specifiedTag) {
                value = item.getValue();
            }
        }

        return value;
    }

    @Override
    public void fromByteArray(byte[] fieldData, ByteOrder order){
        numFields = getFieldsCount(fieldData, order);
        getFields(fieldData, order);
    }

    @Override
    public byte[] toByteArray(ByteOrder order){
        byte[] count;
        int byteCount;
        ByteBuffer bytes;

        count = createIFDEntryCountBytes(order);
        byteCount = IFD_ENTRY_COUNT_LENGTH + (fields.size() * FieldInformation.ENTRY_LENGTH) + IFD_BUFFER_LENGTH;
        bytes = ByteBuffer.allocate(byteCount);
        bytes.order(order);
        bytes.put(count);
        for (FieldInformation item : fields){
            bytes.put(item.toByteArray(order));
        }
        bytes.put(createIFDBuffer(order));

        return bytes.array();
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private byte[] createIFDBuffer(ByteOrder order){
        ByteBuffer bytes;

        bytes = ByteBuffer.allocate(IFD_BUFFER_LENGTH);
        bytes.order(order);

        for (int i = 0; i < IFD_BUFFER_LENGTH; i++){
            bytes.put((byte)0);
        }

        return bytes.array();
    }

    private byte[] createIFDEntryCountBytes(ByteOrder order){
        ByteBuffer bytes;

        bytes = ByteBuffer.allocate(IFD_ENTRY_COUNT_LENGTH);
        bytes.order(order);
        bytes.putShort((short)fields.size());

        return bytes.array();
    }

    private void getFields(byte[] bytes, ByteOrder byteOrder){
        int cursor;

        cursor = 2;
        for (int i = 0; i < numFields; i++){
            byte[] fieldBytes = new byte[FIELD_ENTRY_LENGTH];
            System.arraycopy(bytes, cursor, fieldBytes, 0, FIELD_ENTRY_LENGTH);
            FieldInformation newField = new FieldInformation();
            newField.fromByteArray(fieldBytes, byteOrder);
            fields.add(newField);
            cursor += FIELD_ENTRY_LENGTH;
        }
    }

    private int getFieldsCount(byte[] bytes, ByteOrder byteOrder){
        SignedShortWrapper _fieldsCount;

        _fieldsCount = new SignedShortWrapper(byteOrder);
        System.arraycopy(bytes, 0, _fieldsCount.getDataBytes(), 0, 2);

        return _fieldsCount.get();
    }

}
