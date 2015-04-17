package edu.hipsec.xrdtiffoperations.datamapping;

import edu.hipsec.xrdtiffoperations.datamapping.base.TwoDimensionalMapping;

public class TwoDimensionalDoubleMapping extends TwoDimensionalMapping<Double> {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TwoDimensionalDoubleMapping(int height, int width) {
        dataMap = new Double[height][width];
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

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
    public void rotateDataGrid(double angle) {

        Double[][] newMapping = new Double[this.getHeight()][this.getWidth()];
        Double[] linearTemp = new Double[this.getHeight() * this.getWidth()];

        if (angle > 0.0) {

            for (int y = 0; y < this.getHeight(); y++)
                for (int x = 0; x < this.getWidth(); x++)
                    linearTemp[y * this.getWidth() + x] = this.dataMap[y][x];

            for (int x = this.getWidth() - 1; x > 0; x--)
                for (int y = 0; y < this.getHeight(); y++)
                    newMapping[y][x] = linearTemp[y * this.getWidth() + x];


        } else if (angle < 0.0) {

        }

        this.dataMap = newMapping;
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
