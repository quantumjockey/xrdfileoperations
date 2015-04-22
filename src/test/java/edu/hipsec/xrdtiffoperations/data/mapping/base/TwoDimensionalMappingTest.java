package edu.hipsec.xrdtiffoperations.data.mapping.base;

import org.junit.Assert;
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

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void resetDataGridRotation_GridRotatedAnyAngle_FinalRotationZero() {
        this.mapping.rotateDataGrid(270);
        this.mapping.resetDataGridRotation();
        Assert.assertEquals(0.0, this.mapping.getRotationAngle(), 0.0);
    }

    @Test
    public abstract void resetDataGridRotation_GridRotatedAnyAngle_FirstRowUnchanged();

    @Test
    public void resetDataGridRotation_MultipleRotations_FirstRotationZero() {
        this.mapping.rotateDataGrid(90);
        this.mapping.rotateDataGrid(-180);
        this.mapping.rotateDataGrid(270);
        this.mapping.resetDataGridRotation();
        Assert.assertEquals(0.0, this.mapping.getRotationAngle(), 0.0);
    }

    @Test
    public abstract void resetDataGridRotation_MultipleRotations_FirstRowUnchanged();

    @Test
    public void rotateDataGrid_MultipleRotations_RotationValueSumOfRotationArgumentValues() {
        this.mapping.rotateDataGrid(90);
        this.mapping.rotateDataGrid(-180);
        this.mapping.rotateDataGrid(270);
        Assert.assertEquals(180.0, this.mapping.getRotationAngle(), 0.0);
    }

    @Test
    public abstract void rotateDataGrid_NegativeNinetyDegrees_FirstColumnReversedIsFirstRow();

    @Test
    public abstract void rotateDataGrid_NegativeOneHundredEightyDegrees_LastRowReversedIsFirstRow();

    @Test
    public abstract void rotateDataGrid_NegativeTwoHundredSeventyDegrees_LastColumnIsFirstRow();

    @Test
    public void rotateDataGrid_NonRightAngle_RoundsInputToNearestRightAngle() {
        this.mapping.rotateDataGrid(136);
        Assert.assertEquals(180.0, this.mapping.getRotationAngle(), 0.0);
    }

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
