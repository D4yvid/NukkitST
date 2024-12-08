package org.crimsonmc.block;

import org.crimsonmc.item.ItemTool;

/**
 * Created on 2015/11/25 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockStairsNetherBrick extends BlockStairs {

    public BlockStairsNetherBrick() {
        this(0);
    }

    public BlockStairsNetherBrick(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return NETHER_BRICKS_STAIRS;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Nether Bricks Stairs";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.NETHERRACK_BLOCK_COLOR;
    }
}
