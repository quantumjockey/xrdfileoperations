package edu.hipsec.xrdtiffoperations.twodimensionalmapping;

public class TwoDimensionalDoubleMapping {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final double VALUE_MAXIMUM = Double.MAX_VALUE;
    private final double VALUE_MINIMUM = Double.MIN_VALUE;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected double[][] dataMap;

    // Added to prevent "variable should be effectively final" compilation errors when passing local variables into lambdas.
    private double valueMax;
    private double valueMin;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public double get(int y, int x) {
        if (y < this.getHeight() || y >= 0 || x < this.getWidth() || x >= 0)
            return this.dataMap[y][x];
        else
            return 0;
    }

    /////////// Mutators ////////////////////////////////////////////////////////////////////

    public void setMapCoordinate(int y, int x, int value) {
        this.dataMap[y][x] = value;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TwoDimensionalDoubleMapping(int height, int width) {
        dataMap = new double[height][width];
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void cycleMap(EnvyForCSharpDelegates action) {
        // Issues with multi-threaded execution in JavaFX will be resolved in the future, but
        // are being tabled for purposes of implementing additional features at this time.
        //if (!useMultiThreading)
        this.cycleColumns(0, this.getHeight(), action);
        //else
        //    cycleMapConcurrent(action);
    }

    public double getMaxValue() {
        this.valueMax = this.VALUE_MINIMUM;
        this.cycleMap((y, x) -> {
            if (this.dataMap[y][x] > this.valueMax)
                this.valueMax = this.dataMap[y][x];
        });
        return this.valueMax;
    }

    public double getMinValue() {
        this.valueMin = this.VALUE_MAXIMUM;
        this.cycleMap((y, x) -> {
            if (this.dataMap[y][x] < this.valueMin)
                this.valueMin = this.dataMap[y][x];
        });
        return this.valueMin;
    }

    public int getHeight() {
        return this.dataMap.length;
    }

    public int getWidth() {
        return this.dataMap[0].length;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void cycleColumns(int startRow, int endRow, EnvyForCSharpDelegates action) {
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
