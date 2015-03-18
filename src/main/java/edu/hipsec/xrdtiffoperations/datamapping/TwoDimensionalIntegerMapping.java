package edu.hipsec.xrdtiffoperations.datamapping;

import edu.hipsec.xrdtiffoperations.datamapping.base.TwoDimensionalMapping;

public class TwoDimensionalIntegerMapping extends TwoDimensionalMapping<Integer> {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TwoDimensionalIntegerMapping(int height, int width) {
        dataMap = new Integer[height][width];
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

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
    protected Integer getMaxLimit(){
        return Integer.MAX_VALUE;
    }

    @Override
    protected Integer getMinLimit(){
        return Integer.MIN_VALUE;
    }

}
