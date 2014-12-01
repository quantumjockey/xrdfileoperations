package xrdtiffoperations.filehandling.bytewrappers.base;

public class ByteData {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected byte[] dataBytes;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public byte[] getDataBytes(){
        return dataBytes;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ByteData(int numBytes){
        this.dataBytes = new byte[numBytes];
    }

    public ByteData(){ }

}
