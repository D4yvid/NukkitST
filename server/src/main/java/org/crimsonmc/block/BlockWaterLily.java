package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.level.Level;
import org.crimsonmc.math.AxisAlignedBB;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created on 2015/12/1 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockWaterLily extends BlockFlowable {

    public BlockWaterLily() {
        this(0);
    }

    public BlockWaterLily(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Lily Pad";
    }

    @Override
    public int getId() {
        return WATER_LILY;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x + 0.0625, this.y, this.z + 0.0625, this.x + 0.9375,
                this.y + 0.015625, this.z + 0.9375);
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        if (target instanceof BlockWater) {
            Block up = target.getSide(Vector3.SIDE_UP);
            if (up.getId() == Block.AIR) {
                this.getLevel().setBlock(up, this, true, true);
                return true;
            }
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!(this.getSide(0) instanceof BlockWater)) {
                this.getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{this.getId(), 0, 1}};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}
