package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.imagemodel.header.TiffHeader;
import xrdtiffoperations.imagemodel.ifd.ImageFileDirectory;
import xrdtiffoperations.imagemodel.martiff.components.CalibrationData;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class WritableMARTiffImage extends MARTiffImage{

    /////////// Mutators ///////////////////////////////////////////////////////////////////

    // TiffBase fields

    public void setFilename(String name){
        filename = name;
    }

    public void setHeader(TiffHeader _header){
        header = _header;
    }

    public void setIfdListing(ArrayList<ImageFileDirectory> listing){
        ifdListing = listing;
    }

    // MARTiffImage fields

    public void initializeIntensityMap(int height, int width){
        generatedImage.initializeIntensityMap(height, width);
    }

    public void setCalibration(CalibrationData data){
        generatedImage.setCalibration(data);
    }

    public void setIntensityMapCoordinate(int y, int x, int value) {
        generatedImage.setIntensityMapCoordinate(y, x, value);
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public WritableMARTiffImage(String _filename){
        super(_filename);
    }

}
