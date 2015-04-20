package edu.hipsec.xrdtiffoperations.math;

import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrdtiffoperations.datamodel.FileExtensions;

public class DataMasking {

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static DiffractionFrame maskImage(DiffractionFrame image, int minVal, int maxVal) {
        DiffractionFrame masked = new DiffractionFrame();
        masked.setIdentifier(generateFilename(image.getIdentifier()));
        masked.initializeIntensityMap(image.getHeight(), image.getWidth());
        masked.cycleFramePixels((y, x) ->
                masked.setIntensityMapCoordinate(y, x, maskValue(image.getIntensityMapValue(y, x), maxVal, minVal)));
        return masked;
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private static String generateFilename(String fileName) {
        String baseName, result;

        baseName = stripFilename(fileName);
        result = baseName + FileExtensions.DEFAULT;

        return result;
    }

    private static int maskValue(int _value, int _max, int _min) {
        int value;

        value = _value;

        if (value < _min)
            value = _min;
        else if (value > _max)
            value = _max;

        return value;
    }

    private static String stripFilename(String name) {
        return name.replace(FileExtensions.DEFAULT, "");
    }

}
