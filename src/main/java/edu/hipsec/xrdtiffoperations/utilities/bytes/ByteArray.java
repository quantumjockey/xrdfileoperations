package edu.hipsec.xrdtiffoperations.utilities.bytes;

public class ByteArray {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public static byte[] generateEmptyBytes(int start, int end) {
        int regionLength;
        byte[] emptyBytes;

        regionLength = end - start;
        emptyBytes = new byte[regionLength];

        for (int i = 0; i < regionLength; i++)
            emptyBytes[i] = 0;

        return emptyBytes;
    }

}
