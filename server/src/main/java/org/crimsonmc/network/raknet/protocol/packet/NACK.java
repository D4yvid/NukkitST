package org.crimsonmc.network.raknet.protocol.packet;

import org.crimsonmc.network.raknet.protocol.AcknowledgePacket;
import org.crimsonmc.network.raknet.protocol.Packet;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class NACK extends AcknowledgePacket {

    public static final byte ID = (byte) 0xa0;

    @Override
    public byte getID() {
        return ID;
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new NACK();
        }
    }
}
