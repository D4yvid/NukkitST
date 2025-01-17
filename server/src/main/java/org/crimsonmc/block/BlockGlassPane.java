package org.crimsonmc.block;

import org.crimsonmc.item.Item;

/**
 * Created on 2015/12/6 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockGlassPane extends BlockThin {

    public BlockGlassPane() {
        this(0);
    }

    public BlockGlassPane(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "Glass Pane";
    }

    @Override
    public int getId() {
        return GLASS_PANE;
    }

    @Override
    public double getResistance() {
        return 1.5;
    }

    @Override
    public double getHardness() {
        return 0.3;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[0][0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}
