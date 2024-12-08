package org.crimsonmc.event.redstone;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.block.BlockUpdateEvent;

/**
 * author: Angelic47 crimsonmc Project
 */
public class RedstoneUpdateEvent extends BlockUpdateEvent {

    public RedstoneUpdateEvent(Block source) {
        super(source);
    }
}
