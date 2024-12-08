package org.crimsonmc.item;

/**
 * Created on 2015/12/27 by xtypr. Package cn.crimsonmc.item in project crimsonmc .
 */
public class ItemPotionSplash extends Item {

    public ItemPotionSplash(Integer meta) {
        this(meta, 1);
    }

    public ItemPotionSplash(Integer meta, int count) {
        super(SPLASH_POTION, meta, count, "Splash Potion");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }
}