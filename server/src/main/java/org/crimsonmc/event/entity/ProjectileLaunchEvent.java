package org.crimsonmc.event.entity;

import org.crimsonmc.entity.projectile.EntityProjectile;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;

public class ProjectileLaunchEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public ProjectileLaunchEvent(EntityProjectile entity) {
        this.entity = entity;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public EntityProjectile getEntity() {
        return (EntityProjectile) this.entity;
    }
}
