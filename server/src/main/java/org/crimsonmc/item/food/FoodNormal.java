package org.crimsonmc.item.food;

import org.crimsonmc.player.ServerPlayer;

/**
 * Created by Snake1999 on 2016/1/13. Package cn.crimsonmc.item.food in project crimsonmc.
 */
public class FoodNormal extends Food {

    public FoodNormal(int restoreFood, float restoreSaturation) {
        this.setRestoreFood(restoreFood);
        this.setRestoreSaturation(restoreSaturation);
    }

    @Override
    protected boolean onEatenBy(ServerPlayer player) {
        return super.onEatenBy(player);
    }
}
