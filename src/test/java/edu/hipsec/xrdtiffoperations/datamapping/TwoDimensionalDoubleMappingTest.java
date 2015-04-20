package edu.hipsec.xrdtiffoperations.datamapping;

import edu.hipsec.xrdtiffoperations.datamapping.base.TwoDimensionalMappingTest;
import org.junit.Assert;

public class TwoDimensionalDoubleMappingTest extends TwoDimensionalMappingTest<TwoDimensionalDoubleMapping, Double> {

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Override
    public void resetDataGridRotation_MultipleRotations_FirstRowUnchanged() {
        Double[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(90);
        this.mapping.rotateDataGrid(-180);
        this.mapping.rotateDataGrid(270);
        this.mapping.resetDataGridRotation();
        Double[] firstRowCopy = this.mapping.getRow(0);
        Assert.assertArrayEquals(firstRow, firstRowCopy);
    }

    @Override
    public void resetDataGridRotation_GridRotatedAnyAngle_FirstRowUnchanged() {
        Double[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(90);
        this.mapping.resetDataGridRotation();
        Double[] firstRowCopy = this.mapping.getRow(0);
        Assert.assertArrayEquals(firstRow, firstRowCopy);
    }

    @Override
    public void rotateDataGrid_NegativeNinetyDegrees_FirstColumnReversedIsFirstRow() {
        Double[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(-90);
        Double[] firstColumnReversed = this.reverseOrder(this.mapping.getColumn(0));
        Assert.assertArrayEquals(firstRow, firstColumnReversed);
    }

    @Override
    public void rotateDataGrid_NegativeOneHundredEightyDegrees_LastRowReversedIsFirstRow() {
        Double[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(-180);
        Double[] lastRowReversed = this.reverseOrder(this.mapping.getRow(this.mapping.getHeight() - 1));
        Assert.assertArrayEquals(firstRow, lastRowReversed);
    }

    @Override
    public void rotateDataGrid_NegativeTwoHundredSeventyDegrees_LastColumnIsFirstRow() {
        Double[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(-270);
        Double[] lastColumn = this.mapping.getColumn(this.mapping.getWidth() - 1);
        Assert.assertArrayEquals(firstRow, lastColumn);
    }

    @Override
    public void rotateDataGrid_PositiveNinetyDegrees_LastColumnIsFirstRow() {
        Double[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(90);
        Double[] lastColumn = this.mapping.getColumn(this.mapping.getWidth() - 1);
        Assert.assertArrayEquals(firstRow, lastColumn);
    }

    @Override
    public void rotateDataGrid_PositiveOneHundredEightyDegrees_LastRowReversedIsFirstRow() {
        Double[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(180);
        Double[] lastRowReversed = this.reverseOrder(this.mapping.getRow(this.mapping.getHeight() - 1));
        Assert.assertArrayEquals(firstRow, lastRowReversed);
    }

    @Override
    public void rotateDataGrid_PositiveTwoHundredSeventyDegrees_FirstColumnReversedIsFirstRow() {
        Double[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(270);
        Double[] firstColumnReversed = this.reverseOrder(this.mapping.getColumn(0));
        Assert.assertArrayEquals(firstRow, firstColumnReversed);
    }

    @Override
    public void rotateDataGrid_ZeroDegrees_NoRotation() {
        Double[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(0);
        Double[] firstRowCopy = this.mapping.getRow(0);
        Assert.assertArrayEquals(firstRow, firstRowCopy);
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeMappingForTest() {
        this.mapping = new TwoDimensionalDoubleMapping(3, 3);
        this.mapping.cycleMap((y, x) -> this.mapping.setMapCoordinate(y, x, (y * this.mapping.getWidth() + x) * 1.0));
    }

}
