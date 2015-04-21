package edu.hipsec.xrdtiffoperations.file.mardetector.mar345;

import edu.hipsec.xrdtiffoperations.constants.Diameters;
import edu.hipsec.xrdtiffoperations.constants.FileExtensions;
import edu.hipsec.xrdtiffoperations.constants.PixelSizes;
import edu.hipsec.xrdtiffoperations.file.mardetector.mar345.base.MAR345OutputSchema;

public class MAR3450Image extends MAR345OutputSchema {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeDiameter() {
        this.diameter = Diameters.DIAMETER_345_MM;
    }

    @Override
    protected void initializeExtensions() {
        this.extensions.add(FileExtensions.MAR_3450);
        this.extensions.add(FileExtensions.PCK_3450);
    }

    @Override
    protected void initializePixelSize() {
        this.pixelSize = PixelSizes.ZERO_POINT_TEN_MM;
    }

}
