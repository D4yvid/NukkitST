package org.crimsonmc.network.protocol;

/**
 * @author crimsonmc Project Team
 */
public class AnimatePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ANIMATE_PACKET;

    public long eid;

    public int action;

    @Override
    public void decode() {
        this.action = this.getByte();
        this.eid = getLong();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte) action);
        this.putLong(eid);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
