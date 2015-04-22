package edu.hipsec.xrdtiffoperations.utilities.trigonometry;

public class RoundingOperator {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    static final double RIGHT_ANGLE_INTERVAL = 90.0;

    /////////// Public Methods //////////////////////////////////////////////////////////////

    // preserves simple rotation for square grids
    public static double roundToRightAngle(double input) {
        int numRightAngles = (int)(input / RIGHT_ANGLE_INTERVAL);
        double remainder = input - (numRightAngles * RIGHT_ANGLE_INTERVAL);
        double rounded = input;

        if (remainder != 0.0) {
            if (Math.abs(remainder) >= RIGHT_ANGLE_INTERVAL / 2.0) {
                if (input > 0.0)
                    rounded = input + (RIGHT_ANGLE_INTERVAL - remainder);
                else
                    rounded = input - (RIGHT_ANGLE_INTERVAL + remainder);
            }
            else {
                rounded = numRightAngles * RIGHT_ANGLE_INTERVAL;
            }
        }

        return rounded;
    }

}
