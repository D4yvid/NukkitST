package org.crimsonmc.network.protocol;

import org.crimsonmc.entity.data.EntityMetadata;
import org.crimsonmc.network.binary.Binary;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class AddEntityPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ADD_ENTITY_PACKET;

    public final Object[][] links = new Object[0][3];

    public long eid;

    public int type;

    public float x;

    public float y;

    public float z;

    public float speedX;

    public float speedY;

    public float speedZ;

    public float yaw;

    public float pitch;

    public int modifiers;

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
        this.putInt(this.type);
        this.putFloat(this.x);
        this.putFloat(this.y);
        this.putFloat(this.z);
        this.putFloat(this.speedX);
        this.putFloat(this.speedY);
        this.putFloat(this.speedZ);
        this.putFloat(this.yaw * 0.71f);
        this.putFloat(this.pitch * 0.71f);
        this.putInt(modifiers);
        this.put(Binary.writeMetadata(this.metadata));
        this.putShort(this.links.length);
        for (Object[] link : links) {
            this.putLong((long) link[0]);
            this.putLong((long) link[1]);
            this.putByte((byte) link[2]);
        }
    }
}
