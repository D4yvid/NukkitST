package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.Level;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntityLevelChangeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Level originLevel;

    private final Level targetLevel;

    public EntityLevelChangeEvent(Entity entity, Level originLevel, Level targetLevel) {
        this.entity = entity;
        this.originLevel = originLevel;
        this.targetLevel = targetLevel;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Level getOrigin() {
        return originLevel;
    }

    public Level getTarget() {
        return targetLevel;
    }
}
