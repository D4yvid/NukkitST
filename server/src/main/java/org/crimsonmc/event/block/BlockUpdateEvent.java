package org.crimsonmc.event.block;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockUpdateEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public BlockUpdateEvent(Block block) {
        super(block);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
