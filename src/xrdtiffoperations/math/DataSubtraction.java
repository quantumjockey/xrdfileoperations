package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.FileExtensions;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.imagemodel.martiff.WritableMARTiffImage;

public class DataSubtraction {

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static MARTiffImage subtractImages(MARTiffImage backgroundImage, MARTiffImage diffractionImage){
        String filename;
        int height, width;
        WritableMARTiffImage temp;

        filename = generateFilename(backgroundImage, diffractionImage, false);
        temp = new WritableMARTiffImage(filename);
        temp.setIfdListing(backgroundImage.getIfdListing());
        temp.setHeader(backgroundImage.getHeader());
        temp.setCalibration(backgroundImage.getGeneratedImage().getCalibration());

        height = (backgroundImage.getGeneratedImage().getHeight() < diffractionImage.getGeneratedImage().getHeight()) ? backgroundImage.getGeneratedImage().getHeight() : diffractionImage.getGeneratedImage().getHeight();
        width  = (backgroundImage.getGeneratedImage().getWidth() < diffractionImage.getGeneratedImage().getWidth()) ? backgroundImage.getGeneratedImage().getWidth() : diffractionImage.getGeneratedImage().getWidth();

        temp.initializeIntensityMap(height, width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                temp.setIntensityMapCoordinate(y, x, subtractIntensity(diffractionImage.getGeneratedImage().getIntensityMapValue(y, x), backgroundImage.getGeneratedImage().getIntensityMapValue(y, x)));
            }
        }

        return temp;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////////

    private static String generateFilename(MARTiffImage backgroundImageFile, MARTiffImage diffractionImageFile, boolean longName){
        String firstSegment, result, secondSegment;

        firstSegment = stripFilename(backgroundImageFile.getFilename());
        secondSegment = stripFilename(diffractionImageFile.getFilename());
        if (longName){
            result = firstSegment + "_minus_" + secondSegment + FileExtensions.DEFAULT;
        }
        else{
            String[] suffixParts = secondSegment.split("_");
            result = firstSegment + "_bknd_" + suffixParts[suffixParts.length - 1] + FileExtensions.DEFAULT;
        }

        return result;
    }

    private static String stripFilename(String name){
        return name.replace(FileExtensions.DEFAULT, "");
    }

    private static int subtractIntensity(int firstValue, int secondValue){
        return (firstValue - secondValue);
    }

}
