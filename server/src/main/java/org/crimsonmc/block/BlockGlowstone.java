package org.crimsonmc.block;

import org.crimsonmc.item.Item;

/**
 * Created on 2015/12/6 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockGlowstone extends BlockTransparent {

    public BlockGlowstone() {
        this(0);
    }

    public BlockGlowstone(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "Glowstone";
    }

    @Override
    public int getId() {
        return GLOWSTONE_BLOCK;
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
    public int getLightLevel() {
        return 15;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{Item.GLOWSTONE_DUST, 0, ((int) (2d * Math.random()) + 2)}};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}
