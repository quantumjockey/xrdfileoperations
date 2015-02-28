package edu.hipsec.xrdtiffoperations.datamapping;

import edu.hipsec.xrdtiffoperations.datamapping.base.TwoDimensionalMapping;

public class TwoDimensionalDoubleMapping extends TwoDimensionalMapping<Double> {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TwoDimensionalDoubleMapping(int height, int width) {
        dataMap = new Double[height][width];
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Double getMaxValue() {
        this.valueMax = this.getMinLimit();
        this.cycleMap((y, x) -> {
            if (this.dataMap[y][x] > this.valueMax)
                this.valueMax = this.dataMap[y][x];
        });
        return this.valueMax;
    }

    @Override
    public Double getMinValue() {
        this.valueMin = this.getMaxLimit();
        this.cycleMap((y, x) -> {
            if (this.dataMap[y][x] < this.valueMin)
                this.valueMin = this.dataMap[y][x];
        });
        return this.valueMin;
    }

    @Override
    public Double scaleDataZero() {
        return Math.abs(this.getMinValue());
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected Double getMaxLimit(){
        return Double.MAX_VALUE;
    }

    @Override
    protected Double getMinLimit(){
        return Double.MIN_VALUE;
    }

}
