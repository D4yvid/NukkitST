package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.ServerPlayer;

/**
 * call when a player moves wrongly
 *
 * @author WilliamGao
 * @version 0.1 (23/11/2015)
 */
public class PlayerInvalidMoveEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean revert;

    public PlayerInvalidMoveEvent(ServerPlayer player, boolean revert) {
        this.player = player;
        this.revert = revert;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public boolean isRevert() {
        return this.revert;
    }

    public void setRevert(boolean revert) {
        this.revert = revert;
    }
}
