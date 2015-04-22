package edu.hipsec.xrdtiffoperations.utilities.array;

import java.lang.reflect.Array;

public class ArrayOperator<T> {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected Class<T> genericClassLiteral;

    /////////// Constructor /////////////////////////////////////////////////////////////////

    public ArrayOperator(Class<T> genericClassLiteral) {
        this.genericClassLiteral = genericClassLiteral;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public T[][] deepCopyTwoDimensionalArray(T[][] sourceArray) {
        int arrayHeight = sourceArray.length;
        int arrayWidth = (arrayHeight != 0) ? sourceArray[0].length : 0;
        T[][] temp = this.generateTwoDimensionalTypedArray(arrayHeight, arrayWidth);

        for (int y = 0; y < arrayHeight; y++)
            System.arraycopy(sourceArray[y], 0, temp[y], 0, sourceArray[y].length);

        return temp;
    }

    // shorthand for generating a one-dimensional array
    @SuppressWarnings("unchecked")
    public T[] generateOneDimensionalTypedArray(int size) {
        return (T[]) Array.newInstance(this.genericClassLiteral, (size > 0) ? size : 0);
    }

    // shorthand for generating a two-dimensional array
    @SuppressWarnings("unchecked")
    public T[][] generateTwoDimensionalTypedArray(int ySize, int xSize) {
        return (T[][]) Array.newInstance(this.genericClassLiteral, (ySize > 0) ? ySize : 0, (xSize > 0) ? xSize : 0);
    }

}
