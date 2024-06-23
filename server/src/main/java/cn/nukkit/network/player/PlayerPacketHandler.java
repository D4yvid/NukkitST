package cn.nukkit.network.player;

import cn.nukkit.network.player.handler.LoginPacketHandler;
import cn.nukkit.network.player.handler.MobEquipmentPacketHandler;
import cn.nukkit.network.player.handler.MovePlayerPacketHandler;
import cn.nukkit.network.player.handler.UseItemPacketHandler;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.UseItemPacket;

public class PlayerPacketHandler {

  static PacketHandler<?>[] PACKET_HANDLERS = new PacketHandler[255];

  static {
    PACKET_HANDLERS[LoginPacket.NETWORK_ID] = new LoginPacketHandler();
    PACKET_HANDLERS[MovePlayerPacket.NETWORK_ID] = new MovePlayerPacketHandler();
    PACKET_HANDLERS[MobEquipmentPacket.NETWORK_ID] = new MobEquipmentPacketHandler();
    PACKET_HANDLERS[UseItemPacket.NETWORK_ID] = new UseItemPacketHandler();
  }

  public static PacketHandler<?> getPacketFromPid(int pid) { return PACKET_HANDLERS[pid & 0xFF]; }
}
