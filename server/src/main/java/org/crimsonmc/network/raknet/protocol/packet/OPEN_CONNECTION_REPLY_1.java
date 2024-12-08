package org.crimsonmc.network.raknet.protocol.packet;

import org.crimsonmc.network.raknet.RakNet;
import org.crimsonmc.network.raknet.protocol.Packet;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class OPEN_CONNECTION_REPLY_1 extends Packet {

    public static final byte ID = (byte) 0x06;

    public long serverID;

    public short mtuSize;

    @Override
    public byte getID() {
        return ID;
    }

    @Override
    public void encode() {
        super.encode();
        this.put(RakNet.MAGIC);
        this.putLong(this.serverID);
        this.putByte((byte) 0); // server security
        this.putShort(this.mtuSize);
    }

    @Override
    public void decode() {
        super.decode();
        this.offset += 16; // skip magic bytes
        this.serverID = this.getLong();
        this.getByte(); // skip security
        this.mtuSize = this.getSignedShort();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new OPEN_CONNECTION_REPLY_1();
        }
    }
}
