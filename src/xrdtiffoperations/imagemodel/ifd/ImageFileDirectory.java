package xrdtiffoperations.imagemodel.ifd;

import xrdtiffoperations.imagemodel.ifd.fields.FieldInformation;
import xrdtiffoperations.filehandling.bytewrappers.SignedShortWrapper;

import java.nio.ByteOrder;
import java.util.ArrayList;

public class ImageFileDirectory {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private static int FIELD_ENTRY_LENGTH = 12;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private int numFields;
    private ArrayList<FieldInformation> fields;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public ArrayList<FieldInformation> getFields(){
        return fields;
    }

    /////////// Constructors //////////////////////////////////////////////////////////////////

    public ImageFileDirectory(byte[] directoryBytes, ByteOrder order){
        numFields = getFieldsCount(directoryBytes, order);
        fields = new ArrayList<>();
        getFields(directoryBytes, order);
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

    /////////// Private Methods ///////////////////////////////////////////////////////////////

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
