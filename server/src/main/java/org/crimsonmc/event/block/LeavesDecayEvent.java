package org.crimsonmc.event.block;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class LeavesDecayEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public LeavesDecayEvent(Block block) {
        super(block);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
