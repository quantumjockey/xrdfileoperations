package xrdtiffoperations.imagemodel.ifd.fields;

import xrdtiffoperations.filehandling.bytewrappers.SignedIntWrapper;
import xrdtiffoperations.filehandling.bytewrappers.SignedShortWrapper;
import java.nio.ByteOrder;

public class FieldInformation {

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

    public FieldInformation(byte[] fieldData, ByteOrder order){
        SignedShortWrapper _fieldTag, _fieldType;
        SignedIntWrapper _fieldValue, _typeCount;

        _fieldTag = new SignedShortWrapper(order);
        _fieldType = new SignedShortWrapper(order);
        _typeCount = new SignedIntWrapper(order);
        _fieldValue = new SignedIntWrapper(order);

        for (int i = 0; i < 12; i++) {
            if (i < 2){
                _fieldTag.getDataBytes()[i] = fieldData[i];
            }
            else if(i >= 2 && i < 4){
                _fieldType.getDataBytes()[i - 2] = fieldData[i];
            }
            else if(i >= 4 && i < 8){
                _typeCount.getDataBytes()[i - 4] = fieldData[i];
            }
            else if(i >= 8 && i < 12){
                _fieldValue.getDataBytes()[i - 8] = fieldData[i];
            }
        }

        tag = _fieldTag.get();
        type = _fieldType.get();
        count = _typeCount.get();
        value = _fieldValue.get();
    }

}
