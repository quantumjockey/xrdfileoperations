package edu.hipsec.xrdtiffoperations.data;

import edu.hipsec.xrdtiffoperations.file.martiff.attributes.ResolutionAxis;
import edu.hipsec.xrdtiffoperations.data.mapping.TwoDimensionalIntegerMapping;
import edu.hipsec.xrdtiffoperations.utilities.delegate.EnvyForCSharpDelegates;

public class DiffractionFrame {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected String description;
    protected String identifier;
    protected ResolutionAxis imageXResolution;
    protected ResolutionAxis imageYResolution;
    protected TwoDimensionalIntegerMapping intensityMap;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public String getDescription() {
        return this.description;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public ResolutionAxis getImageXResolution() {
        return this.imageXResolution;
    }

    public ResolutionAxis getImageYResolution() {
        return this.imageYResolution;
    }

    public int getIntensityMapValue(int y, int x) {
        return intensityMap.get(y, x);
    }

    /////////// Mutators ////////////////////////////////////////////////////////////////////

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setIdentifier(String id) {
        this.identifier = id;
    }

    public void setImageXResolution(ResolutionAxis xRes) {
        this.imageXResolution = xRes;
    }

    public void setImageYResolution(ResolutionAxis yRes) {
        this.imageYResolution = yRes;
    }

    public void setIntensityMapCoordinate(int y, int x, int value) {
        this.intensityMap.setMapCoordinate(y, x, value);
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DiffractionFrame(String id) {
        this.description = "";
        this.identifier = id;
        this.imageXResolution = new ResolutionAxis();
        this.imageYResolution = new ResolutionAxis();
    }

    public DiffractionFrame() {
        this("");
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void cycleFramePixels(EnvyForCSharpDelegates action) {
        this.intensityMap.cycleMap(action);
    }

    public int getMaxValue() {
        return this.intensityMap.getDynamicMaxValue();
    }

    public int getMinValue() {
        return this.intensityMap.getDynamicMinValue();
    }

    public int getHeight() {
        return this.intensityMap.getHeight();
    }

    public int getWidth() {
        return this.intensityMap.getWidth();
    }

    public void initializeIntensityMap(int height, int width) {
        this.intensityMap = new TwoDimensionalIntegerMapping(height, width);
    }

    public void initializeIntensityMap(Integer[][] sourceArray) {
        int height = sourceArray.length;
        int width = sourceArray[0].length;
        this.intensityMap = new TwoDimensionalIntegerMapping(height, width);
        this.intensityMap.cycleMap((y, x) -> this.intensityMap.setMapCoordinate(y, x, sourceArray[y][x]));
    }

    public void rotate(double angle){
        this.intensityMap.resetDataGridRotation();
        this.intensityMap.rotateDataGrid(angle);
    }

    public int scaleImageZero() {
        return this.intensityMap.scaleDataZero();
    }

}
