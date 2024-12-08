package org.crimsonmc.event.entity;

import org.crimsonmc.entity.item.EntityItem;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemDespawnEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public ItemDespawnEvent(EntityItem item) {
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
