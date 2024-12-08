package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.entity.EntityLiving;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.item.Item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntityDeathEvent extends EntityEvent {

    private static final HandlerList handlers = new HandlerList();

    private Item[] drops = new Item[0];

    public EntityDeathEvent(EntityLiving entity) {
        this(entity, new Item[0]);
    }

    public EntityDeathEvent(EntityLiving entity, Item[] drops) {
        this.entity = entity;
        this.drops = drops;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public Entity getEntity() {
        return super.getEntity();
    }

    public Item[] getDrops() {
        return drops;
    }

    public void setDrops(Item[] drops) {
        this.drops = drops;
    }
}
