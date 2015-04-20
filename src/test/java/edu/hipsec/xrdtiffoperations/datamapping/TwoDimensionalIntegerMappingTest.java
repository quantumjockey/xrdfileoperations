package edu.hipsec.xrdtiffoperations.datamapping;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;

public class TwoDimensionalIntegerMappingTest {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    TwoDimensionalIntegerMapping mapping;

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        this.mapping = new TwoDimensionalIntegerMapping(3, 3);
        this.mapping.cycleMap((y, x) -> this.mapping.setMapCoordinate(y, x, (y * this.mapping.getWidth() + x)));
    }

    @After
    public void tearDown() throws Exception {

    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void rotateDataGrid_NegativeNinetyDegrees_FirstColumnReversedIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(-90);
        Integer[] firstColumnReversed = this.reverseOrder(this.mapping.getColumn(0));
        Assert.assertArrayEquals(firstRow, firstColumnReversed);
    }

    @Test
    public void rotateDataGrid_NegativeOneHundredEightyDegrees_LastRowReversedIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(-180);
        Integer[] lastRowReversed = this.reverseOrder(this.mapping.getRow(this.mapping.getHeight() - 1));
        Assert.assertArrayEquals(firstRow, lastRowReversed);
    }

    @Test
    public void rotateDataGrid_NegativeTwoHundredSeventyDegrees_LastColumnIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(-270);
        Integer[] lastColumn = this.mapping.getColumn(this.mapping.getWidth() - 1);
        Assert.assertArrayEquals(firstRow, lastColumn);
    }

    @Test
    public void rotateDataGrid_PositiveNinetyDegrees_LastColumnIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(90);
        Integer[] lastColumn = this.mapping.getColumn(this.mapping.getWidth() - 1);
        Assert.assertArrayEquals(firstRow, lastColumn);
    }

    @Test
    public void rotateDataGrid_PositiveOneHundredEightyDegrees_LastRowReversedIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(180);
        Integer[] lastRowReversed = this.reverseOrder(this.mapping.getRow(this.mapping.getHeight() - 1));
        Assert.assertArrayEquals(firstRow, lastRowReversed);
    }

    @Test
    public void rotateDataGrid_PositiveTwoHundredSeventyDegrees_FirstColumnReversedIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(270);
        Integer[] firstColumnReversed = this.reverseOrder(this.mapping.getColumn(0));
        Assert.assertArrayEquals(firstRow, firstColumnReversed);
    }

    @Test
    public void rotateDataGrid_ZeroDegrees_NoRotation() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(0);
        Integer[] firstRowCopy = this.mapping.getRow(0);
        Assert.assertArrayEquals(firstRow, firstRowCopy);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private Integer[] reverseOrder(Integer[] temp){
        Collections.reverse(Arrays.asList(temp));
        return temp;
    }

}
