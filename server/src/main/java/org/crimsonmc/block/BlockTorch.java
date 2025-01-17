package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.level.Level;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created on 2015/12/2 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockTorch extends BlockFlowable {

    public BlockTorch() {
        this(0);
    }

    public BlockTorch(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Torch";
    }

    @Override
    public int getId() {
        return TORCH;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            Block below = this.getSide(0);
            int side = this.getDamage();
            int[] faces = new int[]{
                    0, // 0
                    4, // 1
                    5, // 2
                    2, // 3
                    3, // 4
                    0, // 5
                    0  // 6
            };

            if (this.getSide(faces[side]).isTransparent() &&
                    !(side == 0 && (below instanceof BlockFence || below.getId() == COBBLE_WALL))) {
                this.getLevel().useBreakOn(this);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        Block below = this.getSide(0);

        if (!target.isTransparent() && face != 0) {
            int[] faces = new int[]{
                    0, // 0, nerver used
                    5, // 1
                    4, // 2
                    3, // 3
                    2, // 4
                    1, // 5
            };
            this.meta = faces[face];
            this.getLevel().setBlock(block, this, true, true);

            return true;
        } else if (!below.isTransparent() || below instanceof BlockFence ||
                below.getId() == COBBLE_WALL) {
            this.meta = 0;
            this.getLevel().setBlock(block, this, true, true);

            return true;
        }
        return false;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{this.getId(), 0, 1}};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}
