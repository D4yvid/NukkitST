package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.Location;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntityTeleportEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Location from;

    private Location to;

    public EntityTeleportEvent(Entity entity, Location from, Location to) {
        this.entity = entity;
        this.from = from;
        this.to = to;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }
}
