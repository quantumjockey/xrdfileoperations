package xrdtiffoperations.math;

import xrdtiffoperations.imagemodel.FileExtensions;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.imagemodel.martiff.WritableMARTiffImage;

public class DataMasking {

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public static MARTiffImage maskImage(MARTiffImage image, int minVal, int maxVal){
        String name;
        WritableMARTiffImage temp;

        temp = (WritableMARTiffImage)image;
        name = generateFilename(image);
        temp.setFilename(name);
        for(int y = 0; y < temp.getDiffractionData().getHeight(); y++){
            for (int x = 0; x < temp.getDiffractionData().getWidth(); x++){
                temp.getDiffractionData().setIntensityMapCoordinate(y, x, maskValue(temp.getDiffractionData().getIntensityMapValue(y, x), maxVal, minVal));
            }
        }

        return temp;
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private static String generateFilename(MARTiffImage file){
        String baseName, result;

        baseName = stripFilename(file.getFilename());
        result = baseName + FileExtensions.DEFAULT;

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
        return name.replace(FileExtensions.DEFAULT, "");
    }

}
