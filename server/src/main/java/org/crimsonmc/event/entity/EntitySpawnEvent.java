package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.entity.EntityCreature;
import org.crimsonmc.entity.EntityHuman;
import org.crimsonmc.entity.item.EntityItem;
import org.crimsonmc.entity.item.EntityVehicle;
import org.crimsonmc.entity.projectile.EntityProjectile;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.Position;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntitySpawnEvent extends EntityEvent {

    private static final HandlerList handlers = new HandlerList();

    private final int entityType;

    public EntitySpawnEvent(Entity entity) {
        this.entity = entity;
        this.entityType = entity.getNetworkId();
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Position getPosition() {
        return this.entity.getPosition();
    }

    public int getType() {
        return this.entityType;
    }

    public boolean isCreature() {
        return this.entity instanceof EntityCreature;
    }

    public boolean isHuman() {
        return this.entity instanceof EntityHuman;
    }

    public boolean isProjectile() {
        return this.entity instanceof EntityProjectile;
    }

    public boolean isVehicle() {
        return this.entity instanceof EntityVehicle;
    }

    public boolean isItem() {
        return this.entity instanceof EntityItem;
    }
}
