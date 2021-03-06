package edu.hipsec.xrdtiffoperations.bytewrappers.base;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class ByteWrapper<T> extends ByteData {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected ByteBuffer buffer;
    private ByteOrder order;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ByteWrapper(int bitCount, ByteOrder _order) {
        super();
        this.order = _order;
        this.dataBytes = new byte[this.getByteCount(bitCount)];
    }

    public ByteWrapper(int bitCount){
        this(bitCount, ByteOrder.nativeOrder());
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void extractFromSourceArray(byte[] sourceArray, int startPos){
        System.arraycopy(sourceArray, startPos, this.dataBytes, 0, this.dataBytes.length);
    }

    public abstract T get();

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    protected void wrap() {
        this.buffer = ByteBuffer.wrap(this.dataBytes);
        this.buffer.order(this.order);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private int getByteCount(int sizeInBits) {
        return sizeInBits / Byte.SIZE;
    }

}
