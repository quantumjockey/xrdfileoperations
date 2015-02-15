package xrdtiffoperations.data;

import xrdtiffoperations.imagemodel.attributes.ResolutionAxis;

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
        return description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ResolutionAxis getImageXResolution(){
        return imageXResolution;
    }

    public ResolutionAxis getImageYResolution(){
        return imageYResolution;
    }

    public int getIntensityMapValue(int y, int x){
        return intensityMap[y][x];
    }

    /////////// Mutators ////////////////////////////////////////////////////////////////////

    public void setDescription(String desc){
        description = desc;
    }

    public void setIdentifier(String id){
        identifier = id;
    }

    public void setImageXResolution(ResolutionAxis xRes){
        imageXResolution = xRes;
    }

    public void setImageYResolution(ResolutionAxis yRes){
        imageYResolution = yRes;
    }

    public void setIntensityMapCoordinate(int y, int x, int value){
        intensityMap[y][x] = value;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DiffractionFrame(String id) {
        description = "";
        identifier = id;
        imageXResolution = new ResolutionAxis();
        imageYResolution = new ResolutionAxis();
    }

    public DiffractionFrame() {
        this("");
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void cycleImageDataBytes(EnvyForCSharpDelegates action){
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                try {
                    action.callMethod(y, x);
                }
                catch (Exception e){
                    System.out.println("Error accessing data at pixel (" + y + "," + x + ").");
                    e.printStackTrace();
                }
            }
        }
    }

    public int getMaxValue(){
        intensityMax = INTENSITY_MINIMUM;
        cycleImageDataBytes((y, x) -> {
            if (intensityMap[y][x] > intensityMax) {
                intensityMax = intensityMap[y][x];
            }
        });
        return intensityMax;
    }

    public int getMinValue(){
        intensityMin = INTENSITY_MAXIMUM;
        cycleImageDataBytes((y, x) -> {
            if (intensityMap[y][x] < intensityMin) {
                intensityMin = intensityMap[y][x];
            }
        });
        return intensityMin;
    }

    public int getHeight(){
        return intensityMap.length;
    }

    public int getWidth(){
        return intensityMap[0].length;
    }

    public void initializeIntensityMap(int height, int width){
        intensityMap = new int[height][width];
    }

    /////////// Public Interfaces ///////////////////////////////////////////////////////////

    public interface EnvyForCSharpDelegates {
        void callMethod(int a, int b);
    }
}
