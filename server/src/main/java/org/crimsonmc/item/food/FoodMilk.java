package org.crimsonmc.item.food;

import org.crimsonmc.item.ItemBucket;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created by Snake1999 on 2016/1/21. Package cn.crimsonmc.item.food in project crimsonmc.
 */
public class FoodMilk extends Food {

    @Override
    protected boolean onEatenBy(ServerPlayer player) {
        super.onEatenBy(player);
        player.getInventory().addItem(new ItemBucket());
        player.removeAllEffects();
        return true;
    }
}
