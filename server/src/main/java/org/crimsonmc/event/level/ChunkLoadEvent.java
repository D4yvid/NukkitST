package org.crimsonmc.event.level;

import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.format.FullChunk;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ChunkLoadEvent extends ChunkEvent {

    private static final HandlerList handlers = new HandlerList();

    private final boolean newChunk;

    public ChunkLoadEvent(FullChunk chunk, boolean newChunk) {
        super(chunk);
        this.newChunk = newChunk;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public boolean isNewChunk() {
        return newChunk;
    }
}