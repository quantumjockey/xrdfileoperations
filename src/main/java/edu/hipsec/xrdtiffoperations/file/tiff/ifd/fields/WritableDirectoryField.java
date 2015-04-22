package edu.hipsec.xrdtiffoperations.file.tiff.ifd.fields;

public class WritableDirectoryField extends DirectoryField {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void setTag(short _value) {
        this.tag = _value;
    }

    public void setType(short _value) {
        this.type = _value;
    }

    public void setCount(int _value) {
        this.count = _value;
    }

    public void setValue(int _value) {
        this.value = _value;
    }

}
