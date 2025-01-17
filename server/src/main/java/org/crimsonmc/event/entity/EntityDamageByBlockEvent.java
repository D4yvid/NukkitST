package org.crimsonmc.event.entity;

import org.crimsonmc.block.Block;
import org.crimsonmc.entity.Entity;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntityDamageByBlockEvent extends EntityDamageEvent {

    private final Block damager;

    public EntityDamageByBlockEvent(Block damager, Entity entity, int cause, float damage) {
        super(entity, cause, damage);
        this.damager = damager;
    }

    public Block getDamager() {
        return damager;
    }
}
