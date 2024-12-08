package org.crimsonmc.event.server;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.network.protocol.DataPacket;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class DataPacketSendEvent extends ServerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final DataPacket packet;

    private final ServerPlayer player;

    public DataPacketSendEvent(ServerPlayer player, DataPacket packet) {
        this.packet = packet;
        this.player = player;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public DataPacket getPacket() {
        return packet;
    }
}
