package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.ServerPlayer;

public class PlayerToggleSneakEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected final boolean isSneaking;

    public PlayerToggleSneakEvent(ServerPlayer player, boolean isSneaking) {
        this.player = player;
        this.isSneaking = isSneaking;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public boolean isSneaking() {
        return this.isSneaking;
    }
}
