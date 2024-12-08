package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.math.Vector3;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntityMotionEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Vector3 motion;

    public EntityMotionEvent(Entity entity, Vector3 motion) {
        this.entity = entity;
        this.motion = motion;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Deprecated
    public Vector3 getVector() {
        return this.motion;
    }

    public Vector3 getMotion() {
        return this.motion;
    }
}
