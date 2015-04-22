package edu.hipsec.xrdtiffoperations.utilities.statistics;

import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrdtiffoperations.utilities.array.ArrayOperator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DescriptiveStatsTest {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    DiffractionFrame frame;
    DescriptiveStats frameStats;

    /////////// Setup/Teardown //////////////////////////////////////////////////////////////

    @Before
    public void setUp() throws Exception {

        int size = 3;
        ArrayOperator<Integer> operator = new ArrayOperator<>(Integer.class);
        Integer[][] dummyData = operator.generateTwoDimensionalTypedArray(size, size);

        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                dummyData[y][x] = (y * size) + x;

        this.frame = new DiffractionFrame("scan_scientfic_thingamabob.marredmytie");
        this.frame.initializeIntensityMap(dummyData);
        this.frameStats = new DescriptiveStats(this.frame);
    }

    /////////// Tests ///////////////////////////////////////////////////////////////////////

    @Test
    public void getNumSamplesTotal_EmptyElementsSkipped_ReturnNumberOfElements() {
        Assert.assertEquals(8, this.frameStats.getNumSamplesTotal(true));
    }

    @Test
    public void calculateMean_NoEmptyElementsSkipped_returnMeanForDataset() {
        Assert.assertEquals(4.0, this.frameStats.calculateMean(false), 0.0);
    }

    @Test
    public void calculateStandardDeviation_NoEmptyElementsSkipped_ReturnStandardDeviation() {
        Assert.assertEquals(0.0, this.frameStats.calculateStandardDeviation(false, false), 0.0);
    }

    @Test
    public void calculateSum_NoEmptyElementsSkipped_ReturnSumOfElements() {
        Assert.assertEquals(36, this.frameStats.calculateSum());
    }

    @Test
    public void getNumSamplesTotal_NoEmptyElementsSkipped_ReturnNumberOfElements() {
        Assert.assertEquals(9, this.frameStats.getNumSamplesTotal(false));
    }

    @Test
    public void getNumSamplesWithinInsetCircle_ValidFrame_ReturnNumberOfElements() {
        Assert.assertEquals(7, this.frameStats.getNumSamplesWithinInsetCircle());
    }

}
