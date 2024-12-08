package org.crimsonmc.event.player;

import org.crimsonmc.event.HandlerList;
import org.crimsonmc.lang.TextContainer;
import org.crimsonmc.player.ServerPlayer;

public class PlayerJoinEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    protected TextContainer joinMessage;

    public PlayerJoinEvent(ServerPlayer player, TextContainer joinMessage) {
        this.player = player;
        this.joinMessage = joinMessage;
    }

    public PlayerJoinEvent(ServerPlayer player, String joinMessage) {
        this.player = player;
        this.joinMessage = new TextContainer(joinMessage);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public TextContainer getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(TextContainer joinMessage) {
        this.joinMessage = joinMessage;
    }

    public void setJoinMessage(String joinMessage) {
        this.setJoinMessage(new TextContainer(joinMessage));
    }
}
