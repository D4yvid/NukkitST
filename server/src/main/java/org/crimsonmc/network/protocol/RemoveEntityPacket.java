package org.crimsonmc.network.protocol;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class RemoveEntityPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.REMOVE_ENTITY_PACKET;

    public long eid;

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
        this.putLong(eid);
    }
}
