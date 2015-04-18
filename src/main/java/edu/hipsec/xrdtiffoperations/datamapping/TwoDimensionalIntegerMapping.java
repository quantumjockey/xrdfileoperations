package edu.hipsec.xrdtiffoperations.datamapping;

import edu.hipsec.xrdtiffoperations.datamapping.base.TwoDimensionalMapping;
import java.lang.reflect.Array;

public class TwoDimensionalIntegerMapping extends TwoDimensionalMapping<Integer> {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TwoDimensionalIntegerMapping(int height, int width) {
        this.dataMap = (Integer[][]) Array.newInstance(Integer.class, height, width);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    protected Integer[] generateOneDimensionalTypedArray(int size) {
        return (Integer[]) Array.newInstance(Integer.class, size);
    }

    @Override
    protected Integer[][] generateTwoDimensionalTypedArray(int ySize, int xSize) {
        return (Integer[][]) Array.newInstance(Integer.class, ySize, xSize);
    }

    @Override
    public Integer[] getColumn(int columnNumber) {
        Integer[] column = (Integer[]) Array.newInstance(Integer.class, this.getHeight());
        for (int y = 0; y < this.getHeight(); y++)
            column[y] = this.dataMap[y][columnNumber];
        return column;
    }

    @Override
    public Integer getDynamicMaxValue() {
        this.valueMax = this.getMinLimit();
        this.cycleMap((y, x) -> {
            if (this.dataMap[y][x] > this.valueMax)
                this.valueMax = this.dataMap[y][x];
        });
        return this.valueMax;
    }

    @Override
    public Integer getDynamicMinValue() {
        this.valueMin = this.getMaxLimit();
        this.cycleMap((y, x) -> {
            if (this.dataMap[y][x] < this.valueMin)
                this.valueMin = this.dataMap[y][x];
        });
        return this.valueMin;
    }

    @Override
    public Integer scaleDataZero() {
        return Math.abs(this.getDynamicMinValue());
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected Integer getMaxLimit() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected Integer getMinLimit() {
        return Integer.MIN_VALUE;
    }

}
