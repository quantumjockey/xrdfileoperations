package edu.hipsec.xrdtiffoperations.datamapping;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TwoDimensionalDoubleMappingTest {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    TwoDimensionalDoubleMapping mapping;

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        this.mapping = new TwoDimensionalDoubleMapping(6, 6);
        this.mapping.cycleMap((y, x) -> {
            this.mapping.setMapCoordinate(y, x, (y * this.mapping.getWidth() + x) * 1.0);
        });
    }

    @After
    public void tearDown() throws Exception {

    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void rotateDataGrid_NinetyDegrees_LastColumnIsFirstRow() {
        Double[] firstRow = this.mapping.getRow(0);
        this.mapping.rotateDataGrid(90);
        Double[] lastColumn = this.mapping.getColumn(this.mapping.getHeight() - 1);
        Assert.assertArrayEquals(firstRow, lastColumn);
    }

}
