package edu.hipsec.xrdtiffoperations.file.mardetector.martiff.imagemodel.ifd;

import edu.hipsec.xrdtiffoperations.file.mardetector.martiff.imagemodel.ifd.fields.DirectoryField;
import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.SignedShortWrapper;
import edu.hipsec.xrdtiffoperations.file.mardetector.martiff.imagemodel.ifd.fields.WritableDirectoryField;
import edu.hipsec.xrdtiffoperations.file.serialization.ByteSerializer;
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

    public ArrayList<DirectoryField> getFields() {
        return this.fields;
    }

    /////////// Constructors //////////////////////////////////////////////////////////////////

    public ImageFileDirectory() {
        this.fields = new ArrayList<>();
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void addEntry(short tag, short type, int count, int value) {
        WritableDirectoryField newField = new WritableDirectoryField();
        newField.setTag(tag);
        newField.setType(type);
        newField.setCount(count);
        newField.setValue(value);
        this.fields.add(newField);
    }

    public static int calculateDirectoryLengthWithoutFieldsCount(int fieldsCount) {
        return (fieldsCount * DirectoryField.BYTE_LENGTH) + END_BUFFER_LENGTH;
    }

    public static int extractNumFields(byte[] imageData, int firstIfdOffset, ByteOrder _byteOrder) {
        SignedShortWrapper _fieldsCount;

        _fieldsCount = new SignedShortWrapper(_byteOrder);
        System.arraycopy(imageData, firstIfdOffset, _fieldsCount.getDataBytes(), 0, ImageFileDirectory.FIELD_COUNT_LENGTH);

        return _fieldsCount.get();
    }

    public int getByteLength() {
        return FIELD_COUNT_LENGTH + (this.fields.size() * DirectoryField.BYTE_LENGTH) + END_BUFFER_LENGTH;
    }

    public int getTagValue(short specifiedTag) {
        DirectoryField selected;
        int value;

        selected = this.getField(specifiedTag);
        value = (selected != null) ? selected.getValue() : -1;

        return value;
    }

    public void removeEntry(short specifiedTag) {
        this.fields.remove(this.getField(specifiedTag));
    }

    public void sort() {
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

    private int calculateNumFields(byte[] bytes) {
        int numFields, totalBytes;

        totalBytes = bytes.length - END_BUFFER_LENGTH;
        numFields = totalBytes / DirectoryField.BYTE_LENGTH;

        return numFields;
    }

    private byte[] createBuffer(ByteOrder order) {
        ByteBuffer bytes;

        bytes = ByteBuffer.allocate(END_BUFFER_LENGTH);
        bytes.order(order);

        for (int i = 0; i < END_BUFFER_LENGTH; i++)
            bytes.put((byte) 0);

        return bytes.array();
    }

    private byte[] createEntryCountBytes(ByteOrder order) {
        ByteBuffer bytes;

        bytes = ByteBuffer.allocate(FIELD_COUNT_LENGTH);
        bytes.order(order);
        bytes.putShort((short) this.fields.size());

        return bytes.array();
    }

    private void extractFields(byte[] bytes, ByteOrder byteOrder) {
        int cursor;

        cursor = 0;

        for (int i = 0; i < this.numFields; i++) {
            byte[] fieldBytes = new byte[DirectoryField.BYTE_LENGTH];
            System.arraycopy(bytes, cursor, fieldBytes, 0, DirectoryField.BYTE_LENGTH);
            DirectoryField newField = new DirectoryField();
            newField.fromByteArray(fieldBytes, byteOrder);
            this.fields.add(newField);
            cursor += DirectoryField.BYTE_LENGTH;
        }
    }

    private DirectoryField getField(short specifiedTag) {
        for (DirectoryField item : this.fields)
            if (item.getTag() == specifiedTag)
                return item;
        return null;
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order) {
        this.numFields = this.calculateNumFields(dataBytes);
        this.extractFields(dataBytes, order);
    }

    @Override
    public byte[] toByteArray(ByteOrder order) {
        byte[] count;
        ByteBuffer bytes;

        count = this.createEntryCountBytes(order);
        bytes = ByteBuffer.allocate(this.getByteLength());
        bytes.order(order);
        bytes.put(count);
        for (DirectoryField item : this.fields)
            bytes.put(item.toByteArray(order));
        bytes.put(this.createBuffer(order));

        return bytes.array();
    }

}
