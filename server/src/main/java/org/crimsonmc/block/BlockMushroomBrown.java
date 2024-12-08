package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.level.Level;
import org.crimsonmc.math.AxisAlignedBB;
import org.crimsonmc.player.ServerPlayer;

/**
 * @author crimsonmc Project Team
 */
public class BlockMushroomBrown extends BlockFlowable {

    public BlockMushroomBrown() {
        this(0);
    }

    public BlockMushroomBrown(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "Brown Mushroom";
    }

    @Override
    public int getId() {
        return BROWN_MUSHROOM;
    }

    @Override
    public int getLightLevel() {
        return 1;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (getSide(0).isTransparent()) {
                getLevel().useBreakOn(this);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        Block down = getSide(0);
        if (!down.isTransparent()) {
            getLevel().setBlock(block, this, true, true);

            return true;
        }
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}
