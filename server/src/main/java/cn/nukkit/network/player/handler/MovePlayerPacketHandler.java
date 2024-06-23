package cn.nukkit.network.player.handler;

import cn.nukkit.Player;
import cn.nukkit.entity.item.EntityBoat;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.player.PacketHandler;
import cn.nukkit.network.protocol.MovePlayerPacket;

public class MovePlayerPacketHandler extends PacketHandler<MovePlayerPacket> {

  @Override
  public boolean handlePacket(Player player, MovePlayerPacket packet) {
    var newPos = new Vector3(packet.x, packet.y - player.getEyeHeight(), packet.z);

    if (!player.isAlive() || !player.isSpawned()) {
      var position = player.getTeleportPosition() == null ? player.getPosition()
                                                          : player.getTeleportPosition();

      player.sendPosition(position, packet.yaw, packet.pitch);

      return true;
    }

    var yaw = packet.yaw % 360;
    var pitch = packet.pitch % 360;

    if (yaw < 0) {
      yaw += 360;
    }

    player.setRotation(yaw, pitch);
    player.setNewPosition(newPos);

    if (player.isRiding()) {
      var entity = player.getRidingEntity();

      if (entity instanceof EntityBoat boat) {
        var position = player.getTemporalVector().setComponents(packet.x, packet.y - 1, packet.z);

        boat.setPositionAndRotation(position, (packet.headYaw + 90) % 360, 0);
      }
    }

    return false;
  }
}
