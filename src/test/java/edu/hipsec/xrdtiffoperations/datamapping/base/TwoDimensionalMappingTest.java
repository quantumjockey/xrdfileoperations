package edu.hipsec.xrdtiffoperations.datamapping.base;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;

public abstract class TwoDimensionalMappingTest<MappingType extends TwoDimensionalMapping, ElementType extends Number> {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected MappingType mapping;

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {
        this.initializeMappingForTest();
    }

    @After
    public void tearDown() throws Exception {

    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public abstract void resetDataGridRotation_GridRotatedAnyAngle_FinalRotationZero();

    @Test
    public abstract void resetDataGridRotation_GridRotatedAnyAngle_FirstRowUnchanged();

    @Test
    public abstract void rotateDataGrid_NegativeNinetyDegrees_FirstColumnReversedIsFirstRow();

    @Test
    public abstract void rotateDataGrid_NegativeOneHundredEightyDegrees_LastRowReversedIsFirstRow();

    @Test
    public abstract void rotateDataGrid_NegativeTwoHundredSeventyDegrees_LastColumnIsFirstRow();

    @Test
    public abstract void rotateDataGrid_PositiveNinetyDegrees_LastColumnIsFirstRow();

    @Test
    public abstract void rotateDataGrid_PositiveOneHundredEightyDegrees_LastRowReversedIsFirstRow();

    @Test
    public abstract void rotateDataGrid_PositiveTwoHundredSeventyDegrees_FirstColumnReversedIsFirstRow();

    @Test
    public abstract void rotateDataGrid_ZeroDegrees_NoRotation();

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    protected abstract void initializeMappingForTest();

    protected ElementType[] reverseOrder(ElementType[] temp) {
        Collections.reverse(Arrays.asList(temp));
        return temp;
    }

}
