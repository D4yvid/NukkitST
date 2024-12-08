package org.crimsonmc.block;

/**
 * Created by Snake1999 on 2016/1/11. Package cn.crimsonmc.block in project crimsonmc
 */
public abstract class BlockWeightedPressurePlate extends BlockTransparent {

    public BlockWeightedPressurePlate(int meta) {
        super(meta);
    }

    public BlockWeightedPressurePlate() {
        this(0);
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }
}
