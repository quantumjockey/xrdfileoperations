package edu.hipsec.xrdtiffoperations.twodimensionalmapping;

public class TwoDimensionalIntegerMapping {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int VALUE_MAXIMUM = Integer.MAX_VALUE;
    private final int VALUE_MINIMUM = Integer.MIN_VALUE;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected int[][] dataMap;

    // Added to prevent "variable should be effectively final" compilation errors when passing local variables into lambdas.
    private int valueMax;
    private int valueMin;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public int get(int y, int x) {
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

    public TwoDimensionalIntegerMapping(int height, int width) {
        dataMap = new int[height][width];
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

    public int getMaxValue() {
        this.valueMax = this.VALUE_MINIMUM;
        this.cycleMap((y, x) -> {
            if (this.dataMap[y][x] > this.valueMax)
                this.valueMax = this.dataMap[y][x];
        });
        return this.valueMax;
    }

    public int getMinValue() {
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
