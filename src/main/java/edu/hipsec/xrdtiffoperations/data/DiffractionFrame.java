package edu.hipsec.xrdtiffoperations.data;

import edu.hipsec.xrdtiffoperations.imagemodel.attributes.ResolutionAxis;

public class DiffractionFrame {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int INTENSITY_MAXIMUM = Integer.MAX_VALUE;
    private final int INTENSITY_MINIMUM = Integer.MIN_VALUE;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected String description;
    protected String identifier;
    protected ResolutionAxis imageXResolution;
    protected ResolutionAxis imageYResolution;
    protected int[][] intensityMap;

    // Added to prevent "variable should be effectively final" compilation errors when passing local variables into lambdas.
    private int intensityMax;
    private int intensityMin;

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
        return this.intensityMap[y][x];
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
        this.intensityMap[y][x] = value;
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
        // Issues with multi-threaded execution in JavaFX will be resolved in the future, but
        // are being tabled for purposes of implementing additional features at this time.
        //if (!useMultiThreading)
        this.cycleMap(0, this.getHeight(), action);
        //else
        //    cycleMapConcurrent(action);
    }

    public int getMaxValue() {
        this.intensityMax = this.INTENSITY_MINIMUM;
        this.cycleFramePixels((y, x) -> {
            if (this.intensityMap[y][x] > this.intensityMax)
                this.intensityMax = this.intensityMap[y][x];
        });
        return this.intensityMax;
    }

    public int getMinValue() {
        this.intensityMin = this.INTENSITY_MAXIMUM;
        this.cycleFramePixels((y, x) -> {
            if (this.intensityMap[y][x] < this.intensityMin)
                this.intensityMin = this.intensityMap[y][x];
        });
        return this.intensityMin;
    }

    public int getHeight() {
        return this.intensityMap.length;
    }

    public int getWidth() {
        return this.intensityMap[0].length;
    }

    public void initializeIntensityMap(int height, int width) {
        this.intensityMap = new int[height][width];
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void cycleMap(int startRow, int endRow, EnvyForCSharpDelegates action) {
        for (int y = startRow; y < endRow; y++)
            this.cycleRow(y, action);
    }

    private void cycleRow(int y, EnvyForCSharpDelegates action) {
        for (int x = 0; x < this.getWidth(); x++) {
            try {
                action.callMethod(y, x);
            } catch (Exception e) {
                System.out.println("Error accessing data at pixel (" + y + "," + x + ").");
                e.printStackTrace();
            }
        }
    }

    /////////// Public Interfaces ///////////////////////////////////////////////////////////

    public interface EnvyForCSharpDelegates {
        void callMethod(int a, int b);
    }

}
