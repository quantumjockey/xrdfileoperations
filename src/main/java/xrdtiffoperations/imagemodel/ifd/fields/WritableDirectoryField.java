package xrdtiffoperations.imagemodel.ifd.fields;

public class WritableDirectoryField extends DirectoryField {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void setTag(short _value) {
        tag = _value;
    }

    public void setType(short _value) {
        type = _value;
    }

    public void setCount(int _value) {
        count = _value;
    }

    public void setValue(int _value) {
        value = _value;
    }

}
