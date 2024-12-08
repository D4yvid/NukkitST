package org.crimsonmc.event.server;

import org.crimsonmc.command.CommandSender;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ServerCommandEvent extends ServerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected final CommandSender sender;

    protected String command;

    public ServerCommandEvent(CommandSender sender, String command) {
        this.sender = sender;
        this.command = command;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
