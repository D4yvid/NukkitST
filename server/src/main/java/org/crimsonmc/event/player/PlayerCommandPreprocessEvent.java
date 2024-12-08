package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.ServerPlayer;

public class PlayerCommandPreprocessEvent extends PlayerMessageEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public PlayerCommandPreprocessEvent(ServerPlayer player, String message) {
        this.player = player;
        this.message = message;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public void setPlayer(ServerPlayer player) {
        this.player = player;
    }
}
