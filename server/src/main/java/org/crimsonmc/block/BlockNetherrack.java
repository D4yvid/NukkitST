package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

/**
 * Created on 2015/12/26 by Pub4Game. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockNetherrack extends BlockSolid {

    public BlockNetherrack() {
        this(0);
    }

    public BlockNetherrack(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return NETHERRACK;
    }

    @Override
    public double getResistance() {
        return 2;
    }

    @Override
    public double getHardness() {
        return 0.4;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Netherrack";
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{{Item.NETHERRACK, 0, 1}};
        } else {
            return new int[0][0];
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.NETHERRACK_BLOCK_COLOR;
    }
}
