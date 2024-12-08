package org.crimsonmc.block;

/**
 * Created on 2015/11/23 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockFenceGateAcacia extends BlockFenceGate {

    public BlockFenceGateAcacia() {
        this(0);
    }

    public BlockFenceGateAcacia(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return FENCE_GATE_ACACIA;
    }

    @Override
    public String getName() {
        return "Acacia Fence Gate";
    }
}
