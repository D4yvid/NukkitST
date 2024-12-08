package org.crimsonmc.network.player;

import org.crimsonmc.network.protocol.DataPacket;
import org.crimsonmc.player.ServerPlayer;

public abstract class PacketHandler<T extends DataPacket> {

    public abstract boolean handlePacket(ServerPlayer player, T packet);
}
