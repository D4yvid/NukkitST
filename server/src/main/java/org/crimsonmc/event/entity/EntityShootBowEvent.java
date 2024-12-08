package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.entity.EntityLiving;
import org.crimsonmc.entity.projectile.EntityProjectile;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.item.Item;

/**
 * author: Box crimsonmc Project
 */
public class EntityShootBowEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Item bow;

    private EntityProjectile projectile;

    private double force;

    public EntityShootBowEvent(EntityLiving shooter, Item bow, EntityProjectile projectile,
                               double force) {
        this.entity = shooter;
        this.bow = bow;
        this.projectile = projectile;
        this.force = force;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public EntityLiving getEntity() {
        return (EntityLiving) this.entity;
    }

    public Item getBow() {
        return this.bow;
    }

    public EntityProjectile getProjectile() {
        return this.projectile;
    }

    public void setProjectile(Entity projectile) {
        if (projectile != this.projectile) {
            if (this.projectile.getViewers().size() == 0) {
                this.projectile.kill();
                this.projectile.close();
            }
            this.projectile = (EntityProjectile) projectile;
        }
    }

    public double getForce() {
        return this.force;
    }

    public void setForce(double force) {
        this.force = force;
    }
}
