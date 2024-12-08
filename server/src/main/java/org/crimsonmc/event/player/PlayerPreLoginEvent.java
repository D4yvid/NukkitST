package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.ServerPlayer;

/**
 * Called when the player logs in, before things have been set up
 */
public class PlayerPreLoginEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected String kickMessage;

    public PlayerPreLoginEvent(ServerPlayer player, String kickMessage) {
        this.player = player;
        this.kickMessage = kickMessage;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public String getKickMessage() {
        return this.kickMessage;
    }

    public void setKickMessage(String kickMessage) {
        this.kickMessage = kickMessage;
    }
}
