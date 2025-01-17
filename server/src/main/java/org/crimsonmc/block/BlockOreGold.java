package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockOreGold extends BlockSolid {

    public BlockOreGold() {
        this(0);
    }

    public BlockOreGold(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return GOLD_ORE;
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Gold Ore";
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_IRON) {
            return new int[][]{{Item.GOLD_ORE, 0, 1}};
        } else {
            return new int[0][0];
        }
    }
}
