package xrdtiffoperations.imagemodel.ifd;

import xrdtiffoperations.imagemodel.ifd.fields.DirectoryField;
import xrdtiffoperations.filehandling.bytewrappers.SignedShortWrapper;
import xrdtiffoperations.imagemodel.ifd.fields.WritableDirectoryField;
import xrdtiffoperations.imagemodel.serialization.ByteSerializer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ImageFileDirectory extends ByteSerializer {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    public static final int END_BUFFER_LENGTH = 4;
    public static final int FIELD_COUNT_LENGTH = 2;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private int numFields;
    private ArrayList<DirectoryField> fields;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public ArrayList<DirectoryField> getFields(){
        return fields;
    }

    /////////// Constructors //////////////////////////////////////////////////////////////////

    public ImageFileDirectory(){
        fields = new ArrayList<>();
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void addEntry(short tag, short type, int count, int value){
        WritableDirectoryField newField = new WritableDirectoryField();
        newField.setTag(tag);
        newField.setType(type);
        newField.setCount(count);
        newField.setValue(value);
        fields.add(newField);
    }

    public static int calculateDirectoryLengthWithoutFieldsCount(int fieldsCount){
        return (fieldsCount * DirectoryField.BYTE_LENGTH) + END_BUFFER_LENGTH;
    }

    public static int extractNumFields(byte[] imageData, int firstIfdOffset, ByteOrder _byteOrder) {
        SignedShortWrapper _fieldsCount;

        _fieldsCount = new SignedShortWrapper(_byteOrder);
        System.arraycopy(imageData, firstIfdOffset, _fieldsCount.getDataBytes(), 0, ImageFileDirectory.FIELD_COUNT_LENGTH);

        return _fieldsCount.get();
    }

    public int getByteLength(){
        return FIELD_COUNT_LENGTH + (fields.size() * DirectoryField.BYTE_LENGTH) + END_BUFFER_LENGTH;
    }

    public int getTagValue(short specifiedTag){
        DirectoryField selected;
        int value;

        selected = getField(specifiedTag);
        value = (selected != null) ? selected.getValue() : -1;

        return value;
    }

    public void removeEntry(short specifiedTag){
        fields.remove(getField(specifiedTag));
    }

    public void sort(){
        Collections.sort(fields, new Comparator<DirectoryField>() {
            @Override
            public int compare(DirectoryField o1, DirectoryField o2) {
                Short tagOne = o1.getTag();
                Short tagTwo = o2.getTag();
                return tagOne.compareTo(tagTwo);
            }
        });
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private int calculateNumFields(byte[] bytes){
        int numFields, totalBytes;

        totalBytes = bytes.length - END_BUFFER_LENGTH;
        numFields = totalBytes / DirectoryField.BYTE_LENGTH;

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
            byte[] fieldBytes = new byte[DirectoryField.BYTE_LENGTH];
            System.arraycopy(bytes, cursor, fieldBytes, 0, DirectoryField.BYTE_LENGTH);
            DirectoryField newField = new DirectoryField();
            newField.fromByteArray(fieldBytes, byteOrder);
            fields.add(newField);
            cursor += DirectoryField.BYTE_LENGTH;
        }
    }

    private DirectoryField getField(short specifiedTag){
        for (DirectoryField item : fields){
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
        for (DirectoryField item : fields){
            bytes.put(item.toByteArray(order));
        }
        bytes.put(createBuffer(order));

        return bytes.array();
    }

}
