package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemDye;
import org.crimsonmc.level.Level;
import org.crimsonmc.math.AxisAlignedBB;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created on 2015/11/24 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockCarpet extends BlockFlowable {

    public BlockCarpet() {
        this(0);
    }

    public BlockCarpet(int meta) {
        super(meta);
    }

    public BlockCarpet(ItemDye.DyeColor dyeColor) {
        this(dyeColor.getWoolData());
    }

    @Override
    public int getId() {
        return CARPET;
    }

    @Override
    public double getHardness() {
        return 0.1;
    }

    @Override
    public double getResistance() {
        return 0.5;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public String getName() {
        return ItemDye.DyeColor.getByWoolData(meta) + " Carpet";
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {

        return new AxisAlignedBB(this.x, this.y, this.z, this.x + 1, this.y + 0.0625, this.z + 1);
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        Block down = this.getSide(0);
        if (down.getId() != Item.AIR) {
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.getSide(0).getId() == Item.AIR) {
                this.getLevel().useBreakOn(this);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }

        return 0;
    }

    @Override
    public BlockColor getColor() {
        return ItemDye.DyeColor.getByWoolData(meta).getColor();
    }

    public ItemDye.DyeColor getDyeColor() {
        return ItemDye.DyeColor.getByWoolData(meta);
    }
}
