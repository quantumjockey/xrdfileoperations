package xrdtiffoperations.math;

import xrdtiffoperations.data.DiffractionFrame;
import xrdtiffoperations.imagemodel.FileExtensions;

public class DataSubtraction {

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static DiffractionFrame subtractImages(DiffractionFrame backgroundImage, DiffractionFrame diffractionImage) {
        String filename;
        int height, width;
        DiffractionFrame temp;

        filename = generateFilename(backgroundImage.getIdentifier(), diffractionImage.getIdentifier(), false);
        temp = new DiffractionFrame(filename);

        height = (backgroundImage.getHeight() < diffractionImage.getHeight()) ? backgroundImage.getHeight() : diffractionImage.getHeight();
        width = (backgroundImage.getWidth() < diffractionImage.getWidth()) ? backgroundImage.getWidth() : diffractionImage.getWidth();

        temp.initializeIntensityMap(height, width);
        temp.cycleImageDataBytes((y, x) ->
            temp.setIntensityMapCoordinate(y, x, subtractIntensity(diffractionImage.getIntensityMapValue(y, x), backgroundImage.getIntensityMapValue(y, x))));

        return temp;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////////

    private static String generateFilename(String backgroundImageFile, String diffractionImageFile, boolean longName) {
        String firstSegment, result, secondSegment;

        firstSegment = stripFilename(backgroundImageFile);
        secondSegment = stripFilename(diffractionImageFile);

        if (longName)
            result = firstSegment + "_minus_" + secondSegment + FileExtensions.DEFAULT;
        else {
            String[] suffixParts = secondSegment.split("_");
            result = firstSegment + "_bknd_" + suffixParts[suffixParts.length - 1] + FileExtensions.DEFAULT;
        }

        return result;
    }

    private static String stripFilename(String name) {
        return name.replace(FileExtensions.DEFAULT, "");
    }

    private static int subtractIntensity(int firstValue, int secondValue) {
        return (firstValue - secondValue);
    }

}
