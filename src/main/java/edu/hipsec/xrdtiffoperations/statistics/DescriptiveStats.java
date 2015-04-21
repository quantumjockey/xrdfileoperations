package edu.hipsec.xrdtiffoperations.statistics;

import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;

public class DescriptiveStats {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private long sum;

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public long calculateSum(DiffractionFrame data) {
        this.sum = 0;
        data.cycleFramePixels((y, x) -> {
            int value = data.getIntensityMapValue(y, x);
            if (value != 0)
                this.sum += value;
        });
        return this.sum;
    }

    public double calculateMean(DiffractionFrame data) {
        return calculateSum(data) / getNumSamples(data);
    }

    public double calculateStandardDeviation(DiffractionFrame data){
        // requires functionality - will resume once file reads are completely corrected, tested, and available for use.
        return 0.0;
    }

    public long getNumSamples(DiffractionFrame data) {
        int diameter = data.getWidth();
        int radius = diameter / 2;
        double dataArea = Math.PI * (radius ^ 2);
        return Math.round(dataArea);
    }

}
