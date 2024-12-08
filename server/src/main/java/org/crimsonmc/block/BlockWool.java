package org.crimsonmc.block;

import org.crimsonmc.item.ItemDye;
import org.crimsonmc.item.ItemTool;

/**
 * Created on 2015/12/2 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockWool extends BlockSolid {

    public BlockWool() {
        this(0);
    }

    public BlockWool(int meta) {
        super(meta);
    }

    public BlockWool(ItemDye.DyeColor dyeColor) {
        this(dyeColor.getWoolData());
    }

    @Override
    public String getName() {
        return getDyeColor().getName() + " Wool";
    }

    @Override
    public int getId() {
        return WOOL;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHEARS;
    }

    @Override
    public double getHardness() {
        return 0.8;
    }

    @Override
    public double getResistance() {
        return 4;
    }

    @Override
    public int getBurnChance() {
        return 30;
    }

    @Override
    public int getBurnAbility() {
        return 60;
    }

    @Override
    public BlockColor getColor() {
        return ItemDye.DyeColor.getByWoolData(meta).getColor();
    }

    public ItemDye.DyeColor getDyeColor() {
        return ItemDye.DyeColor.getByWoolData(meta);
    }
}
