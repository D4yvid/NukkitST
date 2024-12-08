package org.crimsonmc.block;

/**
 * Created on 2015/11/23 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockFenceGateSpruce extends BlockFenceGate {

    public BlockFenceGateSpruce() {
        this(0);
    }

    public BlockFenceGateSpruce(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return FENCE_GATE_SPRUCE;
    }

    @Override
    public String getName() {
        return "Spruce Fence Gate";
    }
}
