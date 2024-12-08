package org.crimsonmc.event.player;

import org.crimsonmc.event.HandlerList;
import org.crimsonmc.lang.TextContainer;
import org.crimsonmc.player.ServerPlayer;

public class PlayerQuitEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    protected TextContainer quitMessage;

    protected boolean autoSave = true;

    protected String reason;

    public PlayerQuitEvent(ServerPlayer player, TextContainer quitMessage, String reason) {
        this(player, quitMessage, true, reason);
    }

    public PlayerQuitEvent(ServerPlayer player, TextContainer quitMessage) {
        this(player, quitMessage, true);
    }

    public PlayerQuitEvent(ServerPlayer player, String quitMessage, String reason) {
        this(player, quitMessage, true, reason);
    }

    public PlayerQuitEvent(ServerPlayer player, String quitMessage) {
        this(player, quitMessage, true);
    }

    public PlayerQuitEvent(ServerPlayer player, String quitMessage, boolean autoSave, String reason) {
        this(player, new TextContainer(quitMessage), autoSave, reason);
    }

    public PlayerQuitEvent(ServerPlayer player, String quitMessage, boolean autoSave) {
        this(player, new TextContainer(quitMessage), autoSave);
    }

    public PlayerQuitEvent(ServerPlayer player, TextContainer quitMessage, boolean autoSave) {
        this(player, quitMessage, autoSave, "No reason");
    }

    public PlayerQuitEvent(ServerPlayer player, TextContainer quitMessage, boolean autoSave,
                           String reason) {
        this.player = player;
        this.quitMessage = quitMessage;
        this.autoSave = autoSave;
        this.reason = reason;
    }

    public static HandlerList getHandlers() {
        return handlers;
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

    public boolean getAutoSave() {
        return this.autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public void setAutoSave() {
        this.setAutoSave(true);
    }

    public String getReason() {
        return reason;
    }
}
