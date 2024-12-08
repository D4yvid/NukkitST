package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntityCombustByEntityEvent extends EntityCombustEvent {

    protected final Entity combuster;

    public EntityCombustByEntityEvent(Entity combuster, Entity combustee, int duration) {
        super(combustee, duration);
        this.combuster = combuster;
    }

    public Entity getCombuster() {
        return combuster;
    }
}
