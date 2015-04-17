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
        this.mapping.cycleMap((y, x) -> {
            this.mapping.setMapCoordinate(y, x, y * this.mapping.getWidth() + x);
        });
    }

    @After
    public void tearDown() throws Exception {

    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void rotateDataGrid_NinetyDegrees_LastColumnIsFirstRow() {
        Integer[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(90);
        Integer[] lastColumn = this.mapping.getColumn(this.mapping.getHeight() - 1);
        Assert.assertArrayEquals(firstRow, lastColumn);
    }

}
