package edu.hipsec.xrdtiffoperations.utilities.statistics;

import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;

public class DescriptiveStats {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private DiffractionFrame data;

    /////////// Constructor /////////////////////////////////////////////////////////////////

    public DescriptiveStats(DiffractionFrame data) {
        this.data = data;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public long calculateSum() {
        long sum = 0;
        for (int y = 0; y < this.data.getHeight(); y++)
            for (int x = 0; x < this.data.getWidth(); x++)
                sum += this.data.getIntensityMapValue(y, x);
        return sum;
    }

    public double calculateMean(boolean skipEmptyElements) {
        return calculateSum() / getNumSamplesTotal(skipEmptyElements);
    }

    public double calculateStandardDeviation(boolean skipEmptyElements, boolean insetCircleAsSample) {

        double mean = calculateMean(skipEmptyElements);
        long innerSum = 0;
        long numElements = (insetCircleAsSample)
                ? this.getNumSamplesWithinInsetCircle()
                : this.getNumSamplesTotal(skipEmptyElements);

        for (int y = 0; y < this.data.getHeight(); y++)
            for (int x = 0; x < this.data.getWidth(); x++) {
                int value = this.data.getIntensityMapValue(y, x);
                innerSum += ((value - mean) * (value - mean));
            }

        double variance = (1 / numElements) * innerSum;

        return Math.sqrt(variance);
    }

    public long getNumSamplesTotal(boolean skipEmptyElements) {
        long numSamples = 0;
        for (int y = 0; y < this.data.getHeight(); y++)
            for (int x = 0; x < this.data.getWidth(); x++) {
                if (skipEmptyElements) {
                    if (this.data.getIntensityMapValue(y, x) != 0)
                        numSamples++;
                } else
                    numSamples++;
            }
        return numSamples;
    }

    public long getNumSamplesWithinInsetCircle() {
        double diameter = (double) this.data.getWidth();
        double radius = diameter / 2;
        double dataArea = Math.PI * radius * radius;
        return Math.round(dataArea);
    }

}
