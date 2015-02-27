package edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.base;

public class ByteData {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected byte[] dataBytes;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public byte[] getDataBytes() {
        return this.dataBytes;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ByteData(int numBytes) {
        this.dataBytes = new byte[numBytes];
    }

    public ByteData() {
        // left empty so that time, memory not wasted by initializing array here then re-initializing in superclass
    }

}
