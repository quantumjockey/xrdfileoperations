package edu.hipsec.xrdtiffoperations.utilities.trigonometry;

public class RoundingOperator {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    // preserves simple rotation for square grids
    public static double roundToRightAngle(double input) {
        double remainder = input % 90.0;
        double rounded = input;

        if (remainder != 0.0) {
            rounded = input - remainder;
            if (remainder >= 45.0)
                rounded += 90.0;
        }

        return rounded;
    }

}
