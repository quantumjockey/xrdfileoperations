package xrdtiffoperations.imagemodel.martiff;

import xrdtiffoperations.imagemodel.TiffBase;
import xrdtiffoperations.imagemodel.martiff.components.CalibrationData;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MARTiffImage extends TiffBase {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int INTENSITY_MAXIMUM = 65537;
    private final int INTENSITY_MINIMUM = 0;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected CalibrationData calibration;
    protected int[][] intensityMap;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public CalibrationData getCalibration(){
        return calibration;
    }

    public int getIntensityMapValue(int y, int x){
        return intensityMap[y][x];
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffImage(String _filename) {
        super(_filename);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public int getMaxValue(){
        int maxVal = INTENSITY_MINIMUM;
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                if (intensityMap[y][x] > maxVal){
                    maxVal = intensityMap[y][x];
                }
            }
        }
        return maxVal;
    }

    public int getMinValue(){
        int minVal = INTENSITY_MAXIMUM;
        for (int y = 0; y < getHeight(); y++){
            for (int x = 0; x < getWidth(); x++){
                if (intensityMap[y][x] < minVal){
                    minVal = intensityMap[y][x];
                }
            }
        }
        return minVal;
    }

    public int getHeight(){
        return intensityMap.length;
    }

    public int getWidth(){
        return intensityMap[0].length;
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order){
        super.fromByteArray(dataBytes, order);
    }

    @Override
    public byte[] toByteArray(ByteOrder order){
        ByteBuffer bytes;
        byte[] headerAndFirstIFDBytes;

        headerAndFirstIFDBytes = super.toByteArray(order);
        bytes = ByteBuffer.allocate(headerAndFirstIFDBytes.length);
        bytes.order(order);
        bytes.put(headerAndFirstIFDBytes);

        return bytes.array();
    }

}
