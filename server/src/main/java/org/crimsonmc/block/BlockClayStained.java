package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemDye;
import org.crimsonmc.item.ItemTool;

/**
 * Created on 2015/12/2 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockClayStained extends BlockSolid {

    public BlockClayStained() {
        this(0);
    }

    public BlockClayStained(int meta) {
        super(meta);
    }

    public BlockClayStained(ItemDye.DyeColor dyeColor) {
        this(dyeColor.getWoolData());
    }

    @Override
    public String getName() {
        return getDyeColor().getName() + " Stained Clay";
    }

    @Override
    public int getId() {
        return STAINED_CLAY;
    }

    @Override
    public double getHardness() {
        return 1.25;
    }

    @Override
    public double getResistance() {
        return 0.75;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{{Item.STAINED_CLAY, this.meta, 1}};
        } else {
            return new int[0][0];
        }
    }

    @Override
    public BlockColor getColor() {
        return ItemDye.DyeColor.getByWoolData(meta).getColor();
    }

    public ItemDye.DyeColor getDyeColor() {
        return ItemDye.DyeColor.getByWoolData(meta);
    }
}
