package edu.hipsec.xrdtiffoperations.utilities.bytes;

public class ByteArray {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public static byte[] generateEmptyBytes(int start, int end) {
        int regionLength;
        byte[] emptyBytes;

        regionLength = end - start;
        emptyBytes = (regionLength > 0) ? new byte[regionLength] : new byte[0];

        for (int i = 0; i < regionLength; i++)
            emptyBytes[i] = (byte)0;

        return emptyBytes;
    }

}
