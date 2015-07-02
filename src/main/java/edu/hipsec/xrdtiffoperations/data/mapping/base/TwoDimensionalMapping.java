package edu.hipsec.xrdtiffoperations.data.mapping.base;

import edu.hipsec.xrdtiffoperations.utilities.array.ArrayOperator;
import edu.hipsec.xrdtiffoperations.utilities.delegate.TwoIntArgumentDelegate;
import edu.hipsec.xrdtiffoperations.utilities.trigonometry.RoundingOperator;

public abstract class TwoDimensionalMapping<T extends Number> {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected ArrayOperator<T> arrayUtils;
    protected T[][] dataMap;
    protected double rotationAngle;

    // Added to prevent "variable should be effectively final" compilation errors when passing local variables into lambdas.
    protected T valueMax;
    protected T valueMin;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public T get(int y, int x) {
        return this.dataMap[y][x];
    }

    public double getRotationAngle() {
        return this.rotationAngle;
    }

    /////////// Mutators ////////////////////////////////////////////////////////////////////

    public void setMapCoordinate(int y, int x, T value) {
        this.dataMap[y][x] = value;
    }

    /////////// Constructor /////////////////////////////////////////////////////////////////

    public TwoDimensionalMapping(Class<T> derivedClassLiteral, int height, int width) {
        this.arrayUtils = new ArrayOperator<>(derivedClassLiteral);
        this.dataMap = this.arrayUtils.generateTwoDimensionalTypedArray(height, width);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    // applies lambda passed through delegate interface to all elements within the data grid
    public void cycleMap(TwoIntArgumentDelegate action) {
        // Issues with multi-threaded execution in JavaFX will be resolved in the future, but
        // are being tabled for purposes of implementing additional features at this time.
        //if (!useMultiThreading)
        this.cycleRows(0, this.getHeight(), action);
        //else
        //    cycleMapConcurrent(action);
    }

    // retrieves specified column from data grid
    public T[] getColumn(int columnNumber) {
        T[] column = this.arrayUtils.generateOneDimensionalTypedArray(this.getHeight());
        for (int y = 0; y < this.getHeight(); y++)
            column[y] = this.dataMap[y][columnNumber];
        return column;
    }

    public int getHeight() {
        return this.dataMap.length;
    }

    // retrieves specified row from data grid
    public T[] getRow(int rowNumber) {
        return this.dataMap[rowNumber];
    }

    public int getWidth() {
        return this.dataMap[0].length;
    }

    public abstract T getDynamicMaxValue();

    public abstract T getDynamicMinValue();

    // rotates data grid elements to "zero" position, effectively restoring orientation of original data set
    public void resetDataGridRotation() {
        this.rotateDataGrid(this.rotationAngle * -1.0);
    }

    // rotates data grid elements by specified angle, rounded to the nearest right angle
    public void rotateDataGrid(double angle) {
        double roundedAngle = RoundingOperator.roundToRightAngle(angle);
        int numQuarterTurns = Math.abs((int) roundedAngle / 90);
        T[][] cachedMapping = this.arrayUtils.deepCopyTwoDimensionalArray(this.dataMap);

        this.rotationAngle += roundedAngle;

        for (int i = 0; i < numQuarterTurns; i++)
            if (roundedAngle > 0.0)
                cachedMapping = this.rotateDataGridOneQuarterTurn(true, cachedMapping);
            else
                cachedMapping = this.rotateDataGridOneQuarterTurn(false, cachedMapping);

        if (roundedAngle != 0.0)
            this.dataMap = cachedMapping;
    }

    public abstract T scaleDataZero();

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    protected abstract T getMaxLimit();

    protected abstract T getMinLimit();

    /////////// Private Methods /////////////////////////////////////////////////////////////

    // applies lambda passed through delegate interface to specified rows within data grid
    private void cycleRows(int startRow, int endRow, TwoIntArgumentDelegate action) {
        for (int y = startRow; y < endRow; y++)
            this.cycleRow(y, action);
    }

    // applies lambda passed through delegate interface to each element within specified row of data grid
    private void cycleRow(int y, TwoIntArgumentDelegate action) {
        for (int x = 0; x < this.getWidth(); x++) {
            try {
                action.callMethod(y, x);
            } catch (Exception e) {
                System.out.println("Error accessing data at pixel (" + y + "," + x + ").");
                e.printStackTrace();
            }
        }
    }

    // rotates grid 90 degrees in the positive or negative direction
    private T[][] rotateDataGridOneQuarterTurn(boolean isClockwise, T[][] cachedMapping) {
        final T[][] newMapping = this.arrayUtils.generateTwoDimensionalTypedArray(this.getHeight(), this.getWidth());
        final T[] linearTemp = this.arrayUtils.generateOneDimensionalTypedArray(this.getHeight() * this.getWidth());

        this.cycleMap((y, x) -> linearTemp[y * this.getWidth() + x] = cachedMapping[y][x]);

        int z = (isClockwise) ? 0 : linearTemp.length - 1;

        for (int x = this.getWidth() - 1; x >= 0; x--)
            for (int y = 0; y < this.getHeight(); y++) {
                newMapping[y][x] = linearTemp[z];
                if (isClockwise)
                    z++;
                else
                    z--;
            }

        return newMapping;
    }

}
