package org.crimsonmc.network.protocol;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class MobEffectPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MOB_EFFECT_PACKET;

    public static final byte EVENT_ADD = 1;

    public static final byte EVENT_MODIFY = 2;

    public static final byte EVENT_REMOVE = 3;

    public long eid;

    public int eventId;

    public int effectId;

    public int amplifier;

    public boolean particles = true;

    public int duration;

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
        this.putByte((byte) this.eventId);
        this.putByte((byte) this.effectId);
        this.putByte((byte) this.amplifier);
        this.putBoolean(particles);
        this.putInt(this.duration);
    }
}
