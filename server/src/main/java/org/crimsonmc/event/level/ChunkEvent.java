package org.crimsonmc.event.level;

import org.crimsonmc.level.format.FullChunk;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class ChunkEvent extends LevelEvent {

    private final FullChunk chunk;

    public ChunkEvent(FullChunk chunk) {
        super(chunk.getProvider().getLevel());
        this.chunk = chunk;
    }

    public FullChunk getChunk() {
        return chunk;
    }
}
