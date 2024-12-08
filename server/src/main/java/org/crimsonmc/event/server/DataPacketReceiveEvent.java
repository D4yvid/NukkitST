package org.crimsonmc.event.server;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.network.protocol.DataPacket;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class DataPacketReceiveEvent extends ServerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final DataPacket packet;

    private final ServerPlayer player;

    public DataPacketReceiveEvent(ServerPlayer player, DataPacket packet) {
        this.packet = packet;
        this.player = player;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public DataPacket getPacket() {
        return packet;
    }

    public ServerPlayer getPlayer() {
        return player;
    }
}
