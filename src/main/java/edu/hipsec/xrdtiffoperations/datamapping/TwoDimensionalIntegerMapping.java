package edu.hipsec.xrdtiffoperations.datamapping;

import edu.hipsec.xrdtiffoperations.datamapping.base.TwoDimensionalMapping;

public class TwoDimensionalIntegerMapping extends TwoDimensionalMapping<Integer> {

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public TwoDimensionalIntegerMapping(int height, int width) {
        dataMap = new Integer[height][width];
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    @Override
    public Integer[] getColumn(int columnNumber) {
        Integer[] column = new Integer[this.getHeight()];
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
    public void rotateDataGrid(double angle) {

        Integer[][] newMapping = new Integer[this.getHeight()][this.getWidth()];
        Integer[] linearTemp = new Integer[this.getHeight() * this.getWidth()];

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
