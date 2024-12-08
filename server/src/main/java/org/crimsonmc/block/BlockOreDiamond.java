package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.math.RandomUtilities;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockOreDiamond extends BlockSolid {

    public BlockOreDiamond() {
        this(0);
    }

    public BlockOreDiamond(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return DIAMOND_ORE;
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
        return "Diamond Ore";
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_IRON) {
            return new int[][]{{Item.DIAMOND, 0, 1}};
        } else {
            return new int[0][0];
        }
    }

    @Override
    public int getDropExp() {
        return new RandomUtilities().nextRange(3, 7);
    }
}
