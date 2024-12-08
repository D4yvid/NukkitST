package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

/**
 * Created on 2015/12/7 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockNetherBrick extends BlockSolid {

    public BlockNetherBrick() {
        this(0);
    }

    public BlockNetherBrick(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "Nether Bricks";
    }

    @Override
    public int getId() {
        return NETHER_BRICKS;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 10;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{{Item.NETHER_BRICKS, 0, 1}};
        } else {
            return new int[0][0];
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.NETHERRACK_BLOCK_COLOR;
    }
}
