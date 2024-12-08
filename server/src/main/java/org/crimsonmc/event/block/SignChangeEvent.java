package org.crimsonmc.event.block;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class SignChangeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final ServerPlayer player;

    private String[] lines = new String[4];

    public SignChangeEvent(Block block, ServerPlayer player, String[] lines) {
        super(block);
        this.player = player;
        this.lines = lines;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public String[] getLines() {
        return lines;
    }

    public String getLine(int index) {
        return this.lines[index];
    }

    public void setLine(int index, String line) {
        this.lines[index] = line;
    }
}
