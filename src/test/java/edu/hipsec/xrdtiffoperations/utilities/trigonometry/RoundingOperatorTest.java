package edu.hipsec.xrdtiffoperations.utilities.trigonometry;

import org.junit.Assert;
import org.junit.Test;

public class RoundingOperatorTest {

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void roundToRightAngle_45Degrees_roundUp() {
        Assert.assertEquals(90.0, RoundingOperator.roundToRightAngle(45.0), 0.0);
    }

    @Test
    public void roundToRightAngle_largeAngleGreaterThan45DegreeMark_roundUp() {
        Assert.assertEquals(360.0, RoundingOperator.roundToRightAngle(332.0), 0.0);
    }

    @Test
    public void roundToRightAngle_largeAngleLessThan45DegreeMark_roundDown() {
        Assert.assertEquals(90.0, RoundingOperator.roundToRightAngle(114.0), 0.0);
    }

    @Test
    public void roundToRightAngle_negativeAngleGreaterThan45DegreeMark_roundUp() {
        Assert.assertEquals(-360.0, RoundingOperator.roundToRightAngle(-333.0), 0.0);
    }

    @Test
    public void roundToRightAngle_negativeAngleLessThan45DegreeMark_roundDown() {
        Assert.assertEquals(-90.0, RoundingOperator.roundToRightAngle(-114.0), 0.0);
    }

    @Test
    public void roundToRightAngle_smallAngleLessThan45Degrees_roundDown() {
        Assert.assertEquals(0.0, RoundingOperator.roundToRightAngle(35.0), 0.0);
    }

    @Test
    public void roundToRightAngle_smallAngleMoreThan45Degrees_roundUp() {
        Assert.assertEquals(90.0, RoundingOperator.roundToRightAngle(67.0), 0.0);
    }

}
