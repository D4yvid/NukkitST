package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.lang.TextContainer;
import org.crimsonmc.player.ServerPlayer;

public class PlayerKickEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected final String reason;

    protected TextContainer quitMessage;

    public PlayerKickEvent(ServerPlayer player, String reason, TextContainer quitMessage) {
        this.player = player;
        this.quitMessage = quitMessage;
        this.reason = reason;
    }

    public PlayerKickEvent(ServerPlayer player, String reason, String quitMessage) {
        this.player = player;
        this.quitMessage = new TextContainer(quitMessage);
        this.reason = reason;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public String getReason() {
        return reason;
    }

    public TextContainer getQuitMessage() {
        return quitMessage;
    }

    public void setQuitMessage(TextContainer quitMessage) {
        this.quitMessage = quitMessage;
    }

    public void setQuitMessage(String joinMessage) {
        this.setQuitMessage(new TextContainer(joinMessage));
    }
}
