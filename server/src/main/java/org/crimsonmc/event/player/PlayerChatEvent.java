package org.crimsonmc.event.player;

import org.crimsonmc.command.CommandSender;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.permission.Permissible;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.server.Server;

import java.util.HashSet;
import java.util.Set;

public class PlayerChatEvent extends PlayerMessageEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected String format;

    protected Set<CommandSender> recipients = new HashSet<>();

    public PlayerChatEvent(ServerPlayer player, String message) {
        this(player, message, "chat.type.text", null);
    }

    public PlayerChatEvent(ServerPlayer player, String message, String format,
                           Set<CommandSender> recipients) {
        this.player = player;
        this.message = message;

        this.format = format;

        if (recipients == null) {
            for (Permissible permissible :
                    Server.getInstance().getPluginManager().getPermissionSubscriptions(
                            Server.BROADCAST_CHANNEL_USERS)) {
                if (permissible instanceof CommandSender) {
                    this.recipients.add((CommandSender) permissible);
                }
            }

        } else {
            this.recipients = recipients;
        }
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Changes the player that is sending the message
     */
    public void setPlayer(ServerPlayer player) {
        this.player = player;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Set<CommandSender> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(Set<CommandSender> recipients) {
        this.recipients = recipients;
    }
}
