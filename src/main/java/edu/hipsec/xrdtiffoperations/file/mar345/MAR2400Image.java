package edu.hipsec.xrdtiffoperations.file.mar345;

import edu.hipsec.xrdtiffoperations.constants.Diameters;
import edu.hipsec.xrdtiffoperations.constants.FileExtensions;
import edu.hipsec.xrdtiffoperations.constants.PixelSizes;
import edu.hipsec.xrdtiffoperations.file.mar345.base.MAR345OutputSchema;

public class MAR2400Image extends MAR345OutputSchema {

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void initializeDiameter() {
        this.diameter = Diameters.DIAMETER_240_MM;
    }

    @Override
    protected void initializeExtensions() {
        this.extensions.add(FileExtensions.MAR_2400);
        this.extensions.add(FileExtensions.PCK_2400);
    }

    @Override
    protected void initializePixelSize() {
        this.pixelSize = PixelSizes.ZERO_POINT_TEN_MM;
    }

}
