package org.crimsonmc.event.level;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.format.FullChunk;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ChunkUnloadEvent extends ChunkEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public ChunkUnloadEvent(FullChunk chunk) {
        super(chunk);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}