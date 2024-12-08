package org.crimsonmc.item.food;

import org.crimsonmc.item.ItemBowl;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created by Snake1999 on 2016/1/14. Package cn.crimsonmc.item.food in project crimsonmc.
 */
public class FoodInBowl extends Food {

    public FoodInBowl(int restoreFood, float restoreSaturation) {
        this.setRestoreFood(restoreFood);
        this.setRestoreSaturation(restoreSaturation);
    }

    @Override
    protected boolean onEatenBy(ServerPlayer player) {
        super.onEatenBy(player);
        player.getInventory().addItem(new ItemBowl());
        return true;
    }
}
