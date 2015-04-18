package edu.hipsec.xrdtiffoperations.datamapping;

import edu.hipsec.xrdtiffoperations.datamapping.base.TwoDimensionalMapping;
import java.lang.reflect.Array;

public class TwoDimensionalDoubleMapping extends TwoDimensionalMapping<Double> {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TwoDimensionalDoubleMapping(int height, int width) {
        dataMap = new Double[height][width];
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    protected Double[] generateOneDimensionalTypedArray(int size) {
        return (Double[]) Array.newInstance(Double.class, size);
    }

    @Override
    protected Double[][] generateTwoDimensionalTypedArray(int ySize, int xSize) {
        return (Double[][]) Array.newInstance(Double.class, ySize, xSize);
    }

    @Override
    public Double[] getColumn(int columnNumber) {
        Double[] column = new Double[this.getHeight()];
        for (int y = 0; y < this.getHeight(); y++)
            column[y] = this.dataMap[y][columnNumber];
        return column;
    }

    @Override
    public Double getDynamicMaxValue() {
        this.valueMax = this.getMinLimit();
        this.cycleMap((y, x) -> {
            if (this.dataMap[y][x] > this.valueMax)
                this.valueMax = this.dataMap[y][x];
        });
        return this.valueMax;
    }

    @Override
    public Double getDynamicMinValue() {
        this.valueMin = this.getMaxLimit();
        this.cycleMap((y, x) -> {
            if (this.dataMap[y][x] < this.valueMin)
                this.valueMin = this.dataMap[y][x];
        });
        return this.valueMin;
    }

    @Override
    public Double scaleDataZero() {
        return Math.abs(this.getDynamicMinValue());
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected Double getMaxLimit() {
        return Double.MAX_VALUE;
    }

    @Override
    protected Double getMinLimit() {
        return Double.MIN_VALUE;
    }

}
