package org.crimsonmc.event.player;

import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.Position;
import org.crimsonmc.player.ServerPlayer;

public class PlayerRespawnEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private Position position;

    public PlayerRespawnEvent(ServerPlayer player, Position position) {
        this.player = player;
        this.position = position;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Position getRespawnPosition() {
        return position;
    }

    public void setRespawnPosition(Position position) {
        this.position = position;
    }
}
