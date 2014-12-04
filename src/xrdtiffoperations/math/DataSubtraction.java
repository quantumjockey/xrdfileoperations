package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.imagemodel.martiff.WritableMARTiffImage;

public class DataSubtraction {

    /////////// Constants /////////////////////////////////////////////////////////////////////

    private static final String DEFAULT_EXTENSION = ".tif";

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static MARTiffImage subtractImages(MARTiffImage darkFieldImage, MARTiffImage diffractionImage){
        String filename;
        int height, width;
        WritableMARTiffImage temp;

        filename = generateFilename(darkFieldImage, diffractionImage, false);
        temp = new WritableMARTiffImage(filename);
        temp.setIfdListing(darkFieldImage.getIfdListing());
        temp.setHeader(darkFieldImage.getHeader());
        temp.setCalibration(darkFieldImage.getCalibration());

        height = (darkFieldImage.getHeight() < diffractionImage.getHeight()) ? darkFieldImage.getHeight() : diffractionImage.getHeight();
        width  = (darkFieldImage.getWidth() < diffractionImage.getWidth()) ? darkFieldImage.getWidth() : diffractionImage.getWidth();

        temp.initializeIntensityMap(height, width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                temp.setIntensityMapCoordinate(y, x, subtractIntensity(diffractionImage.getIntensityMapValue(y, x), darkFieldImage.getIntensityMapValue(y, x)));
            }
        }

        return temp;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////////

    private static String generateFilename(MARTiffImage darkFieldImageFile, MARTiffImage diffractionImageFile, boolean longName){
        String firstSegment, result, secondSegment;

        firstSegment = stripFilename(darkFieldImageFile.getFilename());
        secondSegment = stripFilename(diffractionImageFile.getFilename());
        if (longName){
            result = firstSegment + "_minus_" + secondSegment + DEFAULT_EXTENSION;
        }
        else{
            String[] suffixParts = secondSegment.split("_");
            result = firstSegment + "_bknd_" + suffixParts[suffixParts.length - 1] + DEFAULT_EXTENSION;
        }

        return result;
    }

    private static String stripFilename(String name){
        return name.replace(DEFAULT_EXTENSION, "");
    }

    private static int subtractIntensity(int firstValue, int secondValue){
        return (firstValue - secondValue);
    }

}
