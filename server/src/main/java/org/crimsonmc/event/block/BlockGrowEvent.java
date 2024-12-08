package org.crimsonmc.event.block;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockGrowEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block newState;

    public BlockGrowEvent(Block block, Block newState) {
        super(block);
        this.newState = newState;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Block getNewState() {
        return newState;
    }
}
