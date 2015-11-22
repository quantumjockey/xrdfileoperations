package edu.hipsec.xrdtiffoperations.utilities.array;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArrayOperatorTest {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    ArrayOperator<Integer> operator;

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        operator = new ArrayOperator<>(Integer.class);
    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void deepCopyTwoDimensionalArray_SourceMatrixEmpty_ReturnEmptyMatrix() {
        Integer[][] matrix = new Integer[0][];
        Assert.assertEquals(0, this.operator.deepCopyTwoDimensionalArray(matrix).length);
    }

    @Test
    public void deepCopyTwoDimensionalArray_SourceMatrixEmpty_ReturnMatrixWithDifferentAddress() {
        Integer[][] matrix = new Integer[0][];
        Assert.assertNotSame(matrix, this.operator.deepCopyTwoDimensionalArray(matrix));
    }

    @Test
    public void deepCopyTwoDimensionalArray_SourceMatrixValid_ReturnMatrixWithSameElements() {
        int size = 3;
        Integer[][] matrix = new Integer[size][size];

        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                matrix[y][x] = (y * size) + y;

        Integer[][] copy = this.operator.deepCopyTwoDimensionalArray(matrix);

        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                Assert.assertEquals(matrix[y][x], copy[y][x]);

    }

    @Test
    public void deepCopyTwoDimensionalArray_SourceMatrixValid_ReturnMatrixWithDifferentAddress() {
        Integer[][] matrix = new Integer[3][3];
        Assert.assertNotSame(matrix, this.operator.deepCopyTwoDimensionalArray(matrix));
    }

    @Test
    public void generateOneDimensionalTypedArray_NegativeArgument_ReturnEmptyArray() {
        Assert.assertEquals(0, this.operator.generateOneDimensionalTypedArray(-8).length);
    }

    @Test
    public void generateOneDimensionalTypedArray_ValidArgument_ReturnArrayOfSpecifiedLength() {
        Assert.assertEquals(6, this.operator.generateOneDimensionalTypedArray(6).length);
    }

    @Test
    public void generateTwoDimensionalTypedArray_NegativeArgument_ReturnEmptyMatrix() {
        Integer[][] arr = this.operator.generateTwoDimensionalTypedArray(-6, -5);
        Assert.assertEquals(0, arr.length);
    }

    @Test
    public void generateTwoDimensionalTypedArray_ValidArgument_ReturnMatrixOfSpecifiedSize() {
        Integer[][] arr = this.operator.generateTwoDimensionalTypedArray(6, 6);
        Assert.assertEquals(6, arr.length);
        for (Integer[] row : arr) {
            Assert.assertEquals(6, row.length);
        }
    }

}
