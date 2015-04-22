package edu.hipsec.xrdtiffoperations.bytewrappers.base;

import org.junit.Test;

public abstract class ByteWrapperTest<T extends ByteWrapper> {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected byte[] bytes;
    protected T wrapper;

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public abstract void constructor_checkArraySize_expectedByteLengthForType();

    @Test
    public abstract void get_valueOfTypeConverted_returnInput();

}
