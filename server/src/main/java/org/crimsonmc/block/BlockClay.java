package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

/**
 * @author crimsonmc Project Team
 */
public class BlockClay extends BlockSolid {

    public BlockClay(int meta) {
        super(0);
    }

    public BlockClay() {
        this(0);
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 3;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHOVEL;
    }

    @Override
    public int getId() {
        return CLAY_BLOCK;
    }

    @Override
    public String getName() {
        return "Clay Block";
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{Item.CLAY, 0, 4}};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CLAY_BLOCK_COLOR;
    }
}
