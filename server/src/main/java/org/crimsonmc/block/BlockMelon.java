package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

import java.util.Random;

/**
 * Created on 2015/12/11 by Pub4Game. Package cn.crimsonmc.block in project crimsonmc .
 */

public class BlockMelon extends BlockSolid {

    public BlockMelon() {
        this(0);
    }

    public BlockMelon(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return MELON_BLOCK;
    }

    public String getName() {
        return "Melon Block";
    }

    public double getHardness() {
        return 1;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{Item.MELON_SLICE, 0, new Random().nextInt(4) + 3}};
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}
