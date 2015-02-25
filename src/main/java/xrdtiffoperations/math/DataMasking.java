package xrdtiffoperations.math;

import xrdtiffoperations.data.DiffractionFrame;
import xrdtiffoperations.imagemodel.FileExtensions;

public class DataMasking {

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static void maskImage(DiffractionFrame image, int minVal, int maxVal) {
        image.setIdentifier(generateFilename(image.getIdentifier()));
        image.cycleFramePixels((y, x) ->
                image.setIntensityMapCoordinate(y, x, maskValue(image.getIntensityMapValue(y, x), maxVal, minVal)));
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
