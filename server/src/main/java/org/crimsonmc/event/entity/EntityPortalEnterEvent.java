package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;

public class EntityPortalEnterEvent extends EntityEvent implements Cancellable {

    public static final int TYPE_NETHER = 0;

    public static final int TYPE_END = 1;

    private static final HandlerList handlers = new HandlerList();

    private final int type;

    public EntityPortalEnterEvent(Entity entity, int type) {
        this.entity = entity;
        this.type = type;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public int getPortalType() {
        return type;
    }
}
