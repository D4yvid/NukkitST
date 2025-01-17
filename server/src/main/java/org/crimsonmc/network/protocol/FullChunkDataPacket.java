package org.crimsonmc.network.protocol;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class FullChunkDataPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.FULL_CHUNK_DATA_PACKET;

    public static final byte ORDER_COLUMNS = 0;

    public static final byte ORDER_LAYERED = 1;

    public int chunkX;

    public int chunkZ;

    public byte order = ORDER_COLUMNS;

    public byte[] data;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putInt(this.chunkX);
        this.putInt(this.chunkZ);
        this.putByte(this.order);
        this.putInt(this.data.length);
        this.put(this.data);
    }
}
