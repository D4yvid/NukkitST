package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntityDamageByChildEntityEvent extends EntityDamageByEntityEvent {

    private final Entity childEntity;

    public EntityDamageByChildEntityEvent(Entity damager, Entity childEntity, Entity entity,
                                          int cause, float damage) {
        super(damager, entity, cause, damage);
        this.childEntity = childEntity;
    }

    public Entity getChild() {
        return childEntity;
    }
}
