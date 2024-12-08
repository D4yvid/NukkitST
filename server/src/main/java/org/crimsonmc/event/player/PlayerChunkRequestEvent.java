package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.ServerPlayer;

public class PlayerChunkRequestEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final int chunkX;

    private final int chunkZ;

    public PlayerChunkRequestEvent(ServerPlayer player, int chunkX, int chunkZ) {
        this.player = player;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }
}
