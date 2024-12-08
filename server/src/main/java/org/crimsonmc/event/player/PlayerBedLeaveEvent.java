package org.crimsonmc.event.player;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.ServerPlayer;

public class PlayerBedLeaveEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Block bed;

    public PlayerBedLeaveEvent(ServerPlayer player, Block bed) {
        this.player = player;
        this.bed = bed;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Block getBed() {
        return bed;
    }
}
