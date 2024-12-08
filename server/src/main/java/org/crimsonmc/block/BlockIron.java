package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

/**
 * author: Angelic47 crimsonmc Project
 */
public class BlockIron extends BlockSolid {

    public BlockIron() {
        this(0);
    }

    public BlockIron(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return IRON_BLOCK;
    }

    @Override
    public String getName() {
        return "Iron Block";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
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
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_STONE) {
            return new int[][]{{Item.IRON_BLOCK, 0, 1}};
        } else {
            return new int[0][0];
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }
}