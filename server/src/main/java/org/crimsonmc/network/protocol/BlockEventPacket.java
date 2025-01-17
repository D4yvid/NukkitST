package org.crimsonmc.network.protocol;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockEventPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.BLOCK_EVENT_PACKET;

    public int x;

    public int y;

    public int z;

    public int case1;

    public int case2;

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
        this.putInt(this.x);
        this.putInt(this.y);
        this.putInt(this.z);
        this.putInt(this.case1);
        this.putInt(this.case2);
    }
}
