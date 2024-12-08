package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.entity.projectile.EntityProjectile;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.MovingObjectPosition;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ProjectileHitEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private MovingObjectPosition movingObjectPosition;

    public ProjectileHitEvent(EntityProjectile entity) {
        this(entity, null);
    }

    public ProjectileHitEvent(EntityProjectile entity, MovingObjectPosition movingObjectPosition) {
        this.entity = entity;
        this.movingObjectPosition = movingObjectPosition;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public Entity getEntity() {
        return super.getEntity();
    }

    public MovingObjectPosition getMovingObjectPosition() {
        return movingObjectPosition;
    }

    public void setMovingObjectPosition(MovingObjectPosition movingObjectPosition) {
        this.movingObjectPosition = movingObjectPosition;
    }
}
