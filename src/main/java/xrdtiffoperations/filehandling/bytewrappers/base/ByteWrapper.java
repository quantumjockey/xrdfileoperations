package xrdtiffoperations.filehandling.bytewrappers.base;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class ByteWrapper<T extends Comparable<T>> extends ByteData {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected ByteBuffer buffer;
    private ByteOrder order;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ByteWrapper(int bitCount, ByteOrder _order) {
        super();
        this.order = _order;
        this.dataBytes = new byte[getByteCount(bitCount)];
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public abstract T get();

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    protected void wrap() {
        this.buffer = ByteBuffer.wrap(dataBytes);
        this.buffer.order(order);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private int getByteCount(int sizeInBits) {
        return sizeInBits / Byte.SIZE;
    }

}
