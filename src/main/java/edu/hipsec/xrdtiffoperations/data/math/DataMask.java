package edu.hipsec.xrdtiffoperations.data.math;

import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;

public class DataMask extends DataMaskSupplement {

    /////////// Fields ////////////////////////////////////////////////////////////////

    private DiffractionFrame target;

    /////////// Constructor ////////////////////////////////////////////////////////////////

    public DataMask(DiffractionFrame _target) {
        this.target = _target;
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public DiffractionFrame mask(int minVal, int maxVal) {
        DiffractionFrame masked = new DiffractionFrame();
        masked.setIdentifier(this.generateFilename(this.target.getIdentifier()));
        masked.initializeIntensityMap(this.target.getHeight(), this.target.getWidth());
        masked.cycleFramePixels((y, x) ->
                masked.setIntensityMapCoordinate(y, x, this.maskValue(this.target.getIntensityMapValue(y, x), maxVal, minVal)));
        return masked;
    }

}
