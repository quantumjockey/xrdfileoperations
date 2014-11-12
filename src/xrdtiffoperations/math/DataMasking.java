package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.imagemodel.martiff.WritableMARTiffImage;

public class DataMasking {

    /////////// Constants /////////////////////////////////////////////////////////////////////

    private static final String DEFAULT_EXTENSION = ".tif";

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static MARTiffImage maskImage(MARTiffImage image, int minVal, int maxVal){
        String name;
        WritableMARTiffImage temp;

        temp = (WritableMARTiffImage)image;
        name = generateFilename(image, minVal, maxVal);
        temp.setFilename(name);
        for(int y = 0; y < temp.getHeight(); y++){
            for (int x = 0; x < temp.getWidth(); x++){
                temp.setIntensityMapCoordinate(y, x, maskValue(temp.getIntensityMapValue(y, x), maxVal, minVal));
            }
        }

        return temp;
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private static String generateFilename(MARTiffImage file, int min, int max){
        String baseName, result;

        baseName = stripFilename(file.getFilename());
        if (max != file.getMaxValue()){
            result = baseName + "_max_" + max + DEFAULT_EXTENSION;
        }
        else if (min != file.getMinValue()){
            result = baseName + "_min_" + min + DEFAULT_EXTENSION;
        }
        else{
            result = baseName + DEFAULT_EXTENSION;
        }

        return result;
    }

    private static int maskValue(int _value, int _max, int _min){
        int value;

        value = _value;
        if (value < _min){
            value = _min;
        }
        else if (value > _max){
            value = _max;
        }

        return value;
    }

    private static String stripFilename(String name){
        return name.replace(DEFAULT_EXTENSION, "");
    }

}
