package org.crimsonmc.network.player;

import org.crimsonmc.network.player.handler.LoginPacketHandler;
import org.crimsonmc.network.player.handler.MobEquipmentPacketHandler;
import org.crimsonmc.network.player.handler.MovePlayerPacketHandler;
import org.crimsonmc.network.player.handler.UseItemPacketHandler;
import org.crimsonmc.network.protocol.LoginPacket;
import org.crimsonmc.network.protocol.MobEquipmentPacket;
import org.crimsonmc.network.protocol.MovePlayerPacket;
import org.crimsonmc.network.protocol.UseItemPacket;

public class PlayerPacketHandler {

    static PacketHandler<?>[] PACKET_HANDLERS = new PacketHandler[255];

    static {
        PACKET_HANDLERS[LoginPacket.NETWORK_ID] = new LoginPacketHandler();
        PACKET_HANDLERS[MovePlayerPacket.NETWORK_ID] = new MovePlayerPacketHandler();
        PACKET_HANDLERS[MobEquipmentPacket.NETWORK_ID] = new MobEquipmentPacketHandler();
        PACKET_HANDLERS[UseItemPacket.NETWORK_ID] = new UseItemPacketHandler();
    }

    public static PacketHandler<?> getPacketFromPid(int pid) {
        return PACKET_HANDLERS[pid & 0xFF];
    }
}
