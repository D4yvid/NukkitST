package org.crimsonmc.event.potion;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.potion.Potion;

/**
 * Created by Snake1999 on 2016/1/12. Package cn.crimsonmc.event.potion in project crimsonmc
 */
public class PotionApplyEvent extends PotionEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Entity entity;

    public PotionApplyEvent(Potion potion, Entity entity) {
        super(potion);
        this.entity = entity;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Entity getEntity() {
        return entity;
    }
}
