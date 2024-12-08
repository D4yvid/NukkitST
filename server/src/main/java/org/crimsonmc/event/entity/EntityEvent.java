package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.event.Event;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class EntityEvent extends Event {

    protected Entity entity;

    public Entity getEntity() {
        return entity;
    }
}
