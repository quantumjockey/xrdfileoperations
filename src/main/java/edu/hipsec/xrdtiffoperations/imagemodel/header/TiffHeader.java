package edu.hipsec.xrdtiffoperations.imagemodel.header;

import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.SignedIntWrapper;
import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.SignedShortWrapper;
import edu.hipsec.xrdtiffoperations.filehandling.bytewrappers.TiffByteOrderWrapper;
import edu.hipsec.xrdtiffoperations.imagemodel.serialization.ByteSerializer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TiffHeader extends ByteSerializer {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final int ORDER_BYTES_LENGTH = 2;
    private final int FILE_ID_BYTES_LENGTH = 2;
    private final int FIRST_IFD_OFFSET_BYTES_LENGTH = 4;

    public static final int BYTE_LENGTH = 8;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private ByteOrder byteOrder;
    private short fileID;
    private int firstIfdOffset;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public ByteOrder getByteOrder(){
        return byteOrder;
    }

    public short getFileID(){
        return fileID;
    }

    public int getFirstIfdOffset(){
        return firstIfdOffset;
    }

    /////////// Mutators ////////////////////////////////////////////////////////////////////

    public void setByteOrder(ByteOrder order){
        byteOrder = order;
    }

    public void setFileID(short id){
        fileID = id;
    }

    public void setFirstIfdOffset(int offset){
        firstIfdOffset = offset;
    }

    /////////// ByteSerializer Methods //////////////////////////////////////////////////////

    @Override
    public void fromByteArray(byte[] dataBytes, ByteOrder order) {
        TiffByteOrderWrapper _byteOrder;
        SignedShortWrapper _identifier;
        SignedIntWrapper _ifdOffset;

        if (dataBytes.length == BYTE_LENGTH) {
            _byteOrder = new TiffByteOrderWrapper();

            // extract byte order
            System.arraycopy(dataBytes, 0, _byteOrder.getDataBytes(), 0, ORDER_BYTES_LENGTH);
            byteOrder = _byteOrder.get();

            _identifier = new SignedShortWrapper(_byteOrder.get());
            _ifdOffset = new SignedIntWrapper(_byteOrder.get());

            // extract file identifier
            System.arraycopy(dataBytes, 2, _identifier.getDataBytes(), 0, FILE_ID_BYTES_LENGTH);
            fileID = _identifier.get();

            // extract first IFD offset value
            System.arraycopy(dataBytes, 4, _ifdOffset.getDataBytes(), 0, FIRST_IFD_OFFSET_BYTES_LENGTH);
            firstIfdOffset = _ifdOffset.get();
        } else
            displaySizeAlert(dataBytes.length, BYTE_LENGTH);

    }

    @Override
    public byte[] toByteArray(ByteOrder order){
        ByteBuffer bytes;
        String id;

        bytes = ByteBuffer.allocate(BYTE_LENGTH);
        bytes.order(order);

        if(order == ByteOrder.BIG_ENDIAN)
            id = ByteOrderString.BIG_ENDIAN_STRING;
        else
            id = ByteOrderString.LITTLE_ENDIAN_STRING;

        bytes.put(id.getBytes());
        bytes.putShort(fileID);
        bytes.putInt(firstIfdOffset);

        return bytes.array();
    }

}
