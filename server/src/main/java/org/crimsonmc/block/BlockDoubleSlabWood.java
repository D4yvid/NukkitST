package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

/**
 * Created on 2015/12/2 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockDoubleSlabWood extends BlockSolid {

    public BlockDoubleSlabWood() {
        this(0);
    }

    public BlockDoubleSlabWood(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return DOUBLE_WOOD_SLAB;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public String getName() {
        String[] names =
                new String[]{"Oak", "Spruce", "Birch", "Jungle", "Acacia", "Dark Oak", "", ""};
        return "Double " + names[this.meta & 0x07] + " Slab";
    }

    public int[][] getDrops(Item item) {
        return new int[][]{{Item.WOOD_SLAB, this.meta & 0x07, 2}};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }
}
