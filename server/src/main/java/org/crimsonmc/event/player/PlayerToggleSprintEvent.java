package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.ServerPlayer;

public class PlayerToggleSprintEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected final boolean isSprinting;

    public PlayerToggleSprintEvent(ServerPlayer player, boolean isSprinting) {
        this.player = player;
        this.isSprinting = isSprinting;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public boolean isSprinting() {
        return this.isSprinting;
    }
}
