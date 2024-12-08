package org.crimsonmc.event.entity;

import org.crimsonmc.entity.item.EntityItem;
import org.crimsonmc.event.HandlerList;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemSpawnEvent extends EntityEvent {

    private static final HandlerList handlers = new HandlerList();

    public ItemSpawnEvent(EntityItem item) {
        this.entity = item;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public EntityItem getEntity() {
        return (EntityItem) this.entity;
    }
}
