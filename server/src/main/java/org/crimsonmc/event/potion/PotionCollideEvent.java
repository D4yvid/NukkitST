package org.crimsonmc.event.potion;

import org.crimsonmc.entity.item.EntityPotion;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.potion.Potion;

/**
 * Created by Snake1999 on 2016/1/12. Package cn.crimsonmc.event.potion in project crimsonmc
 */
public class PotionCollideEvent extends PotionEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final EntityPotion thrownPotion;

    public PotionCollideEvent(Potion potion, EntityPotion thrownPotion) {
        super(potion);
        this.thrownPotion = thrownPotion;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public EntityPotion getThrownPotion() {
        return thrownPotion;
    }
}
