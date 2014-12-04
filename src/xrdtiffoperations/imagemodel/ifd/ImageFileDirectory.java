package xrdtiffoperations.imagemodel.ifd;

import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import xrdtiffoperations.filehandling.bytewrappers.SignedShortWrapper;
import xrdtiffoperations.imagemodel.ifd.fields.WritableFieldInformation;
import xrdtiffoperations.imagemodel.serialization.ByteSerializer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ImageFileDirectory extends ByteSerializer {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    public static final int END_BUFFER_LENGTH = 4;
    public static final int FIELD_COUNT_LENGTH = 2;

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

    public void addEntry(short tag, short type, int count, int value){
        WritableFieldInformation newField = new WritableFieldInformation();
        newField.setTag(tag);
        newField.setType(type);
        newField.setCount(count);
        newField.setValue(value);
        fields.add(newField);
    }

    public static int calculateDirectoryLengthWithoutFieldsCount(int fieldsCount){
        return (fieldsCount * FieldInformation.BYTE_LENGTH) + END_BUFFER_LENGTH;
    }

    public static int extractNumFields(byte[] imageData, int firstIfdOffset, ByteOrder _byteOrder) {
        SignedShortWrapper _fieldsCount;

        _fieldsCount = new SignedShortWrapper(_byteOrder);
        System.arraycopy(imageData, firstIfdOffset, _fieldsCount.getDataBytes(), 0, ImageFileDirectory.FIELD_COUNT_LENGTH);

        return _fieldsCount.get();
    }

    public int getByteLength(){
        return FIELD_COUNT_LENGTH + (fields.size() * FieldInformation.BYTE_LENGTH) + END_BUFFER_LENGTH;
    }

    public int getTagValue(short specifiedTag){
        FieldInformation selected;
        int value;

        selected = getField(specifiedTag);
        value = (selected != null) ? selected.getValue() : -1;

        return value;
    }

    public void removeEntry(short specifiedTag){
        fields.remove(getField(specifiedTag));
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private int calculateNumFields(byte[] bytes){
        int numFields, totalBytes;

        totalBytes = bytes.length - END_BUFFER_LENGTH;
        numFields = totalBytes / FieldInformation.BYTE_LENGTH;

        return numFields;
    }

    private byte[] createBuffer(ByteOrder order){
        ByteBuffer bytes;

        bytes = ByteBuffer.allocate(END_BUFFER_LENGTH);
        bytes.order(order);

        for (int i = 0; i < END_BUFFER_LENGTH; i++){
            bytes.put((byte)0);
        }

        return bytes.array();
    }

    private byte[] createEntryCountBytes(ByteOrder order){
        ByteBuffer bytes;

        bytes = ByteBuffer.allocate(FIELD_COUNT_LENGTH);
        bytes.order(order);
        bytes.putShort((short)fields.size());

        return bytes.array();
    }

    private void extractFields(byte[] bytes, ByteOrder byteOrder){
        int cursor;

        cursor = 0;

        for (int i = 0; i < numFields; i++){
            byte[] fieldBytes = new byte[FieldInformation.BYTE_LENGTH];
            System.arraycopy(bytes, cursor, fieldBytes, 0, FieldInformation.BYTE_LENGTH);
            FieldInformation newField = new FieldInformation();
            newField.fromByteArray(fieldBytes, byteOrder);
            fields.add(newField);
            cursor += FieldInformation.BYTE_LENGTH;
        }
    }

    private FieldInformation getField(short specifiedTag){
        for (FieldInformation item : fields){
            if (item.getTag() == specifiedTag) {
                return item;
            }
        }
        return null;
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order){
        numFields = calculateNumFields(dataBytes);
        extractFields(dataBytes, order);
    }

    @Override
    public byte[] toByteArray(ByteOrder order){
        byte[] count;
        ByteBuffer bytes;

        count = createEntryCountBytes(order);
        bytes = ByteBuffer.allocate(getByteLength());
        bytes.order(order);
        bytes.put(count);
        for (FieldInformation item : fields){
            bytes.put(item.toByteArray(order));
        }
        bytes.put(createBuffer(order));

        return bytes.array();
    }

}
