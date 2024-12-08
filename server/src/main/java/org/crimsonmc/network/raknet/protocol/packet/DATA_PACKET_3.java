package org.crimsonmc.network.raknet.protocol.packet;

import org.crimsonmc.network.raknet.protocol.DataPacket;
import org.crimsonmc.network.raknet.protocol.Packet;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class DATA_PACKET_3 extends DataPacket {

    public static final byte ID = (byte) 0x83;

    @Override
    public byte getID() {
        return ID;
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new DATA_PACKET_3();
        }
    }
}
