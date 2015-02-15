package xrdtiffoperations.data;

import xrdtiffoperations.imagemodel.martiff.components.CalibrationData;

public class DiffractionFrame {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int INTENSITY_MAXIMUM = Integer.MAX_VALUE;
    private final int INTENSITY_MINIMUM = Integer.MIN_VALUE;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected CalibrationData calibration;
    protected int[][] intensityMap;

    // Added to prevent "variable should be effectively final" compilation errors when passing local variables into lambdas.
    private int intensityMax;
    private int intensityMin;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public CalibrationData getCalibration(){
        return calibration;
    }

    public int getIntensityMapValue(int y, int x){
        return intensityMap[y][x];
    }

    /////////// Mutators ////////////////////////////////////////////////////////////////////

    public void initializeIntensityMap(int height, int width){
        intensityMap = new int[height][width];
    }

    public void setCalibration(CalibrationData data){
        calibration = data;
    }

    public void setIntensityMapCoordinate(int y, int x, int value){
        intensityMap[y][x] = value;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DiffractionFrame() {
        calibration = new CalibrationData();
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

    /////////// Public Interfaces ///////////////////////////////////////////////////////////

    public interface EnvyForCSharpDelegates {
        void callMethod(int a, int b);
    }
}
