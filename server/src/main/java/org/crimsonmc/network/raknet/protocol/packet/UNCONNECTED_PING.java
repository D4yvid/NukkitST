package org.crimsonmc.network.raknet.protocol.packet;

import org.crimsonmc.network.raknet.RakNet;
import org.crimsonmc.network.raknet.protocol.Packet;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class UNCONNECTED_PING extends Packet {

    public static final byte ID = (byte) 0x01;

    public long pingID;

    @Override
    public byte getID() {
        return ID;
    }

    @Override
    public void encode() {
        super.encode();
        this.putLong(this.pingID);
        this.put(RakNet.MAGIC);
    }

    @Override
    public void decode() {
        super.decode();
        this.pingID = this.getLong();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new UNCONNECTED_PING();
        }
    }
}
