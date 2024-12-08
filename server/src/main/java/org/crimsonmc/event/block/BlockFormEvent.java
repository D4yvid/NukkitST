package org.crimsonmc.event.block;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockFormEvent extends BlockGrowEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public BlockFormEvent(Block block, Block newState) {
        super(block, newState);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
