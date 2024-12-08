package org.crimsonmc.block;

import org.crimsonmc.math.AxisAlignedBB;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class BlockFlowable extends BlockTransparent {

    protected BlockFlowable(int meta) {
        super(meta);
    }

    @Override
    public boolean canBeFlowedInto() {
        return true;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0;
    }

    @Override
    public double getResistance() {
        return 0;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return null;
    }
}
