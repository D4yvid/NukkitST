package org.crimsonmc.network.protocol;

/**
 * @author crimsonmc Project Team
 */
public class RemoveBlockPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.REMOVE_BLOCK_PACKET;

    public long eid;

    public int x;

    public int y;

    public int z;

    @Override
    public void decode() {
        this.eid = this.getLong();
        this.x = this.getInt();
        this.z = getInt();
        this.y = getByte();
    }

    @Override
    public void encode() {
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
