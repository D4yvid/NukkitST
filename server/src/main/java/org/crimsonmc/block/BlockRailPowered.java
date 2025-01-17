package org.crimsonmc.block;

import org.crimsonmc.item.Item;

/**
 * Created by Snake1999 on 2016/1/11. Package cn.crimsonmc.block in project crimsonmc
 */
public class BlockRailPowered extends BlockRail {

    public BlockRailPowered() {
        this(0);
    }

    public BlockRailPowered(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return POWERED_RAIL;
    }

    @Override
    public String getName() {
        return "Powered Rail";
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{Item.POWERED_RAIL, 0, 1}};
    }
}
