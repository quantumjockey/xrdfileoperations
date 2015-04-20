package edu.hipsec.xrdtiffoperations.datamapping.base;

import java.lang.reflect.Array;

public abstract class TwoDimensionalMapping<T extends Number> {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected Class<T> derivedClassLiteral;
    protected T[][] dataMap;
    protected double rotationAngle;

    // Added to prevent "variable should be effectively final" compilation errors when passing local variables into lambdas.
    protected T valueMax;
    protected T valueMin;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public T get(int y, int x) {
        return this.dataMap[y][x];
    }

        return this.rotationAngle;
    }

    /////////// Mutators ////////////////////////////////////////////////////////////////////

    public void setMapCoordinate(int y, int x, T value) {
        this.dataMap[y][x] = value;
    }

    /////////// Constructor /////////////////////////////////////////////////////////////////

    public TwoDimensionalMapping(Class<T> derivedClassLiteral, int height, int width) {
        this.derivedClassLiteral = derivedClassLiteral;
        this.dataMap = this.generateTwoDimensionalTypedArray(height, width);
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

    public T[] getColumn(int columnNumber) {
        T[] column = this.generateOneDimensionalTypedArray(this.getHeight());
        for (int y = 0; y < this.getHeight(); y++)
            column[y] = this.dataMap[y][columnNumber];
        return column;
    }

    public int getHeight() {
        return this.dataMap.length;
    }

    public T[] getRow(int rowNumber) {
        return this.dataMap[rowNumber];
    }

    public int getWidth() {
        return this.dataMap[0].length;
    }

    public abstract T getDynamicMaxValue();

    public abstract T getDynamicMinValue();

    }

    public void rotateDataGrid(double angle) {
        int numQuarterTurns = Math.abs((int) angle / 90);
        T[][] cachedMapping = this.deepCopyTwoDimensionalArray(this.dataMap);

        this.rotationAngle += angle;

        for (int i = 0; i < numQuarterTurns; i++)
            if (angle > 0.0)
                cachedMapping = rotateDataGridOneQuarterTurn(true, cachedMapping);
            else
                cachedMapping = rotateDataGridOneQuarterTurn(false, cachedMapping);

        if (angle != 0.0)
            this.dataMap = cachedMapping;
    }

    public abstract T scaleDataZero();

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    protected abstract T getMaxLimit();

    protected abstract T getMinLimit();

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

    @SuppressWarnings("unchecked")
    private T[] generateOneDimensionalTypedArray(int size) {
        return (T[]) Array.newInstance(this.derivedClassLiteral, size);
    }

    @SuppressWarnings("unchecked")
    private T[][] generateTwoDimensionalTypedArray(int ySize, int xSize) {
        return (T[][]) Array.newInstance(this.derivedClassLiteral, ySize, xSize);
    }

    private T[][] rotateDataGridOneQuarterTurn(boolean isClockwise, T[][] cachedMapping) {
        final T[][] newMapping = this.generateTwoDimensionalTypedArray(this.getHeight(), this.getWidth());
        final T[] linearTemp = this.generateOneDimensionalTypedArray(this.getHeight() * this.getWidth());

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

    /////////// Generically Applicable Methods //////////////////////////////////////////////

    private T[][] deepCopyTwoDimensionalArray(T[][] sourceArray) {
        int arrayHeight = sourceArray.length;
        int arrayWidth = sourceArray[0].length;

        T[][] temp = this.generateTwoDimensionalTypedArray(arrayHeight, arrayWidth);

        for (int y = 0; y < arrayHeight; y++)
            System.arraycopy(sourceArray[y], 0, temp[y], 0, sourceArray[y].length);

        return temp;
    }

    /////////// Public Interfaces ///////////////////////////////////////////////////////////

    public interface EnvyForCSharpDelegates {
        void callMethod(int a, int b);
    }

}
