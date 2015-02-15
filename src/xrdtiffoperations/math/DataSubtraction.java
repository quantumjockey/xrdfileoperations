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
        temp.getDiffractionData().setCalibration(backgroundImage.getDiffractionData().getCalibration());

        height = (backgroundImage.getDiffractionData().getHeight() < diffractionImage.getDiffractionData().getHeight()) ? backgroundImage.getDiffractionData().getHeight() : diffractionImage.getDiffractionData().getHeight();
        width  = (backgroundImage.getDiffractionData().getWidth() < diffractionImage.getDiffractionData().getWidth()) ? backgroundImage.getDiffractionData().getWidth() : diffractionImage.getDiffractionData().getWidth();

        temp.getDiffractionData().initializeIntensityMap(height, width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                temp.getDiffractionData().setIntensityMapCoordinate(y, x, subtractIntensity(diffractionImage.getDiffractionData().getIntensityMapValue(y, x), backgroundImage.getDiffractionData().getIntensityMapValue(y, x)));
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
