package org.crimsonmc.event.block;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockSpreadEvent extends BlockFormEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block source;

    public BlockSpreadEvent(Block block, Block source, Block newState) {
        super(block, newState);
        this.source = source;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Block getSource() {
        return source;
    }
}
