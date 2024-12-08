package org.crimsonmc.network.protocol;

import org.crimsonmc.entity.data.EntityMetadata;
import org.crimsonmc.network.binary.Binary;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class SetEntityDataPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_ENTITY_DATA_PACKET;

    public long eid;

    public EntityMetadata metadata;

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
        this.putLong(this.eid);
        this.put(Binary.writeMetadata(this.metadata));
    }
}
