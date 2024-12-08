package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.math.AxisAlignedBB;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class BlockStairs extends BlockTransparent {

    protected BlockStairs(int meta) {
        super(meta);
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x, this.y, this.z, this.x + 1,
                this.y + 1, // or 0.5, but for on ground checking must be 1
                this.z + 1);
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz) {
        return this.place(item, block, target, face, fx, fy, fz, null);
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        int[] faces = new int[]{0, 2, 1, 3};
        this.meta = (faces[player.getDirection()] & 0x03);
        if ((fy > 0.5 && face != 1) || face == 0) {
            this.meta |= 0x04; // Upside-down stairs
        }
        this.getLevel().setBlock(block, this, true, true);

        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{{this.getId(), 0, 1}};
        } else {
            return new int[0][0];
        }
    }
}
