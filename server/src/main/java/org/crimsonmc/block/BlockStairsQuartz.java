package org.crimsonmc.block;

import org.crimsonmc.item.ItemTool;

/**
 * Created on 2015/11/25 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockStairsQuartz extends BlockStairs {

    public BlockStairsQuartz() {
        this(0);
    }

    public BlockStairsQuartz(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return QUARTZ_STAIRS;
    }

    @Override
    public double getHardness() {
        return 0.8;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Quartz Stairs";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.QUARTZ_BLOCK_COLOR;
    }
}
