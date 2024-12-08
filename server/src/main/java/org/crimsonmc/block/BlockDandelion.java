package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.level.Level;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created on 2015/12/2 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockDandelion extends BlockFlowable {

    public BlockDandelion() {
        this(0);
    }

    public BlockDandelion(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "Dandelion";
    }

    @Override
    public int getId() {
        return DANDELION;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        Block down = this.getSide(0);
        if (down.getId() == 2 || down.getId() == 3 || down.getId() == 60) {
            this.getLevel().setBlock(block, this, true, true);

            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.getSide(0).isTransparent()) {
                this.getLevel().useBreakOn(this);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}
