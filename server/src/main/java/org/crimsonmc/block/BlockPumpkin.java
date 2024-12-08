package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created on 2015/12/8 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockPumpkin extends BlockSolid {

    public BlockPumpkin() {
        this(0);
    }

    public BlockPumpkin(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "Pumpkin";
    }

    @Override
    public int getId() {
        return PUMPKIN;
    }

    @Override
    public double getHardness() {
        return 1;
    }

    @Override
    public double getResistance() {
        return 5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        if (player != null) {
            if (player.getDirection() != null) {
                this.meta = (player.getDirection() + 5) % 4;
            }
        }
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}
