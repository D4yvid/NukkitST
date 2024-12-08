package org.crimsonmc.event.block;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.Event;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class BlockEvent extends Event {

    protected final Block block;

    public BlockEvent(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
