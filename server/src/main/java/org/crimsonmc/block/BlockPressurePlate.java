package org.crimsonmc.block;

/**
 * Created by Snake1999 on 2016/1/11. Package cn.crimsonmc.block in project crimsonmc
 */
public abstract class BlockPressurePlate extends BlockTransparent {

    protected BlockPressurePlate() {
        this(0);
    }

    protected BlockPressurePlate(int meta) {
        super(meta);
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    // todo redstone here?
    // todo bounding box
}
