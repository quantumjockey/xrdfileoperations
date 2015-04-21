package edu.hipsec.xrdtiffoperations.data.mapping;

import edu.hipsec.xrdtiffoperations.data.mapping.base.TwoDimensionalMapping;

public class TwoDimensionalDoubleMapping extends TwoDimensionalMapping<Double> {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TwoDimensionalDoubleMapping(int height, int width) {
        super(Double.class, height, width);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

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
