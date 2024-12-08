package org.crimsonmc.item;

import org.crimsonmc.block.Block;
import org.crimsonmc.level.Level;
import org.crimsonmc.player.ServerPlayer;

public class ItemGlassBottle extends Item {

    public ItemGlassBottle() {
        this(0, 1);
    }

    public ItemGlassBottle(Integer meta) {
        this(meta, 1);
    }

    public ItemGlassBottle(Integer meta, int count) {
        super(GLASS_BOTTLE, meta, count, "Glass Bottle");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, ServerPlayer player, Block block, Block target, int face,
                              double fx, double fy, double fz) {
        if (target.getId() == WATER || target.getId() == STILL_WATER) {
            if (player.getInventory().canAddItem(Item.get(Item.POTION))) {
                Item item = player.getInventory().getItemInHand();
                item.setCount(item.getCount() - 1);
                player.getInventory().setItemInHand(item);
                player.getInventory().addItem(Item.get(Item.POTION));
                return true;
            }
        }
        return false;
    }
}
