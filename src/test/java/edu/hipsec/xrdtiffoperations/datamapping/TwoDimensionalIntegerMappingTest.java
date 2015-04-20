package edu.hipsec.xrdtiffoperations.datamapping;

import edu.hipsec.xrdtiffoperations.datamapping.base.TwoDimensionalMappingTest;
import org.junit.Assert;

public class TwoDimensionalIntegerMappingTest extends TwoDimensionalMappingTest<TwoDimensionalIntegerMapping, Integer> {

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Override
    public void rotateDataGrid_NegativeNinetyDegrees_FirstColumnReversedIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(-90);
        Integer[] firstColumnReversed = this.reverseOrder(this.mapping.getColumn(0));
        Assert.assertArrayEquals(firstRow, firstColumnReversed);
    }

    @Override
    public void rotateDataGrid_NegativeOneHundredEightyDegrees_LastRowReversedIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(-180);
        Integer[] lastRowReversed = this.reverseOrder(this.mapping.getRow(this.mapping.getHeight() - 1));
        Assert.assertArrayEquals(firstRow, lastRowReversed);
    }

    @Override
    public void rotateDataGrid_NegativeTwoHundredSeventyDegrees_LastColumnIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(-270);
        Integer[] lastColumn = this.mapping.getColumn(this.mapping.getWidth() - 1);
        Assert.assertArrayEquals(firstRow, lastColumn);
    }

    @Override
    public void rotateDataGrid_PositiveNinetyDegrees_LastColumnIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(90);
        Integer[] lastColumn = this.mapping.getColumn(this.mapping.getWidth() - 1);
        Assert.assertArrayEquals(firstRow, lastColumn);
    }

    @Override
    public void rotateDataGrid_PositiveOneHundredEightyDegrees_LastRowReversedIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(180);
        Integer[] lastRowReversed = this.reverseOrder(this.mapping.getRow(this.mapping.getHeight() - 1));
        Assert.assertArrayEquals(firstRow, lastRowReversed);
    }

    @Override
    public void rotateDataGrid_PositiveTwoHundredSeventyDegrees_FirstColumnReversedIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(270);
        Integer[] firstColumnReversed = this.reverseOrder(this.mapping.getColumn(0));
        Assert.assertArrayEquals(firstRow, firstColumnReversed);
    }

    @Override
    public void rotateDataGrid_ZeroDegrees_NoRotation() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(0);
        Integer[] firstRowCopy = this.mapping.getRow(0);
        Assert.assertArrayEquals(firstRow, firstRowCopy);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeMappingForTest() {
        this.mapping = new TwoDimensionalIntegerMapping(3, 3);
        this.mapping.cycleMap((y, x) -> this.mapping.setMapCoordinate(y, x, (y * this.mapping.getWidth() + x)));
    }

}
