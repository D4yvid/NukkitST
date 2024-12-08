package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.ServerPlayer;

public class PlayerLoginEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected String kickMessage;

    public PlayerLoginEvent(ServerPlayer player, String kickMessage) {
        this.player = player;
        this.kickMessage = kickMessage;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public String getKickMessage() {
        return kickMessage;
    }

    public void setKickMessage(String kickMessage) {
        this.kickMessage = kickMessage;
    }
}
