package org.crimsonmc.event.entity;

import org.crimsonmc.block.Block;
import org.crimsonmc.entity.Entity;

/**
 * author: Box crimsonmc Project
 */
public class EntityCombustByBlockEvent extends EntityCombustEvent {

    protected final Block combuster;

    public EntityCombustByBlockEvent(Block combuster, Entity combustee, int duration) {
        super(combustee, duration);
        this.combuster = combuster;
    }

    public Block getCombuster() {
        return combuster;
    }
}
