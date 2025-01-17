package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

/**
 * Created on 2015/12/6 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockIronBars extends BlockThin {

    public BlockIronBars() {
        this(0);
    }

    public BlockIronBars(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "Iron Bars";
    }

    @Override
    public int getId() {
        return IRON_BARS;
    }

    @Override
    public double getHardness() {
        return 5;
    }

    @Override
    public double getResistance() {
        return 10;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{{Item.IRON_BARS, 0, 1}};
        } else {
            return new int[0][0];
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }
}
