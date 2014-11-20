package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.imagemodel.martiff.WritableMARTiffImage;

public class DataSubtraction {

    /////////// Constants /////////////////////////////////////////////////////////////////////

    private static final String DEFAULT_EXTENSION = ".tif";

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static MARTiffImage subtractImages(MARTiffImage subtractedImage, MARTiffImage baseImage){
        String filename;
        int height, width;
        WritableMARTiffImage temp;

        filename = generateFilename(subtractedImage, baseImage, false);
        temp = new WritableMARTiffImage(filename);
        temp.setIfdListing(subtractedImage.getIfdListing());
        temp.setByteOrder(subtractedImage.getByteOrder());
        temp.setCalibration(subtractedImage.getCalibration());

        height = (subtractedImage.getHeight() < baseImage.getHeight()) ? subtractedImage.getHeight() : baseImage.getHeight();
        width  = (subtractedImage.getWidth() < baseImage.getWidth()) ? subtractedImage.getWidth() : baseImage.getWidth();

        temp.initializeIntensityMap(height, width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                temp.setIntensityMapCoordinate(y, x, subtractIntensity(subtractedImage.getIntensityMapValue(y, x), baseImage.getIntensityMapValue(y, x)));
            }
        }

        return temp;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////////

    private static String generateFilename(MARTiffImage subtractedFile, MARTiffImage baseFile, boolean longName){
        String firstSegment, result, secondSegment;

        firstSegment = stripFilename(subtractedFile.getFilename());
        secondSegment = stripFilename(baseFile.getFilename());
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
