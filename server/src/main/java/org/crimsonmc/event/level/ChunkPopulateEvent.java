package org.crimsonmc.event.level;

import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.format.FullChunk;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ChunkPopulateEvent extends ChunkEvent {

    private static final HandlerList handlers = new HandlerList();

    public ChunkPopulateEvent(FullChunk chunk) {
        super(chunk);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}