package org.crimsonmc.event.potion;

import org.crimsonmc.event.Event;
import org.crimsonmc.potion.Potion;

/**
 * Created by Snake1999 on 2016/1/12. Package cn.crimsonmc.event.potion in project crimsonmc
 */
public abstract class PotionEvent extends Event {

    private Potion potion;

    public PotionEvent(Potion potion) {
        this.potion = potion;
    }

    public Potion getPotion() {
        return potion;
    }

    public void setPotion(Potion potion) {
        this.potion = potion;
    }
}
