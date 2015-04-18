package edu.hipsec.xrdtiffoperations.datamapping;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TwoDimensionalIntegerMappingTest {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    TwoDimensionalIntegerMapping mapping;

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        this.mapping = new TwoDimensionalIntegerMapping(6, 6);
        this.mapping.cycleMap((y, x) -> this.mapping.setMapCoordinate(y, x, (y * this.mapping.getWidth() + x)));
    }

    @After
    public void tearDown() throws Exception {

    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void rotateDataGrid_NegativeNinetyDegrees_FirstColumnIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(-90);
        Integer[] firstColumn = this.mapping.getColumn(0);
        Assert.assertArrayEquals(firstRow, firstColumn);
    }

    @Test
    public void rotateDataGrid_NegativeOneHundredEightyDegrees_LastRowIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(-180);
        Integer[] lastRow = this.mapping.getRow(this.mapping.getHeight() - 1);
        Assert.assertArrayEquals(firstRow, lastRow);
    }

    @Test
    public void rotateDataGrid_NegativeTwoHundredSeventyDegrees_LastColumnIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(-270);
        Integer[] firstColumn = this.mapping.getColumn(this.mapping.getWidth() - 1);
        Assert.assertArrayEquals(firstRow, firstColumn);
    }

    @Test
    public void rotateDataGrid_PositiveNinetyDegrees_LastColumnIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(90);
        Integer[] lastColumn = this.mapping.getColumn(this.mapping.getWidth() - 1);
        Assert.assertArrayEquals(firstRow, lastColumn);
    }

    @Test
    public void rotateDataGrid_PositiveOneHundredEightyDegrees_LastRowIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(180);
        Integer[] lastRow = this.mapping.getRow(this.mapping.getHeight() - 1);
        Assert.assertArrayEquals(firstRow, lastRow);
    }

    @Test
    public void rotateDataGrid_PositiveTwoHundredSeventyDegrees_FirstColumnIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(270);
        Integer[] firstColumn = this.mapping.getColumn(0);
        Assert.assertArrayEquals(firstRow, firstColumn);
    }

    @Test
    public void rotateDataGrid_ZeroDegrees_NoRotation() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(0);
        Integer[] firstRowCopy = this.mapping.getRow(0);
        Assert.assertArrayEquals(firstRow, firstRowCopy);
    }

}
