package xrdtiffoperations.filehandling.bytewrappers.base;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteWrapper {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected ByteBuffer buffer;
    protected byte[] dataBytes;
    private ByteOrder order;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public byte[] getDataBytes(){
        return dataBytes;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ByteWrapper(int bitCount, ByteOrder _order){
        this.order = _order;
        dataBytes = new byte[getByteCount(bitCount)];
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    protected void wrap(){
        buffer = ByteBuffer.wrap(dataBytes);
        buffer.order(order);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private int getByteCount(int sizeInBits){
        return sizeInBits / Byte.SIZE;
    }

}
