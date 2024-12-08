package org.crimsonmc.block;

import org.crimsonmc.item.ItemTool;

/**
 * Created on 2015/11/25 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockStairsBrick extends BlockStairs {

    public BlockStairsBrick() {
        this(0);
    }

    public BlockStairsBrick(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BRICK_STAIRS;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Brick Stairs";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.STONE_BLOCK_COLOR;
    }
}
