package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntityRegainHealthEvent extends EntityEvent implements Cancellable {

    public static final int CAUSE_REGEN = 0;

    public static final int CAUSE_EATING = 1;

    public static final int CAUSE_MAGIC = 2;

    public static final int CAUSE_CUSTOM = 3;

    private static final HandlerList handlers = new HandlerList();

    private final int reason;

    private float amount;

    public EntityRegainHealthEvent(Entity entity, float amount, int regainReason) {
        this.entity = entity;
        this.amount = amount;
        this.reason = regainReason;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getRegainReason() {
        return reason;
    }
}
