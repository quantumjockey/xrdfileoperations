package xrdtiffoperations.filehandling.scaling;

public class ScaleData {

    public static byte toByte(int dataValue, int max, int min){
        byte scaledValue;
        float scale;

        scale = (dataValue - min) / (max - min);
        scaledValue = (byte)(scale * (float)Byte.MAX_VALUE);

        return scaledValue;
    }

    public static char toUnsignedShort(int dataValue, int max, int min){
        char scaledValue;
        float scale;

        scale = (dataValue - min) / (max - min);
        scaledValue = (char)(scale * (float)Character.MAX_VALUE);

        return scaledValue;
    }

}
