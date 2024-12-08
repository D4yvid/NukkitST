package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockDirt extends BlockSolid {

    public BlockDirt() {
        this(0);
    }

    public BlockDirt(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return DIRT;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{Item.DIRT, 0, 1}};
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHOVEL;
    }

    @Override
    public String getName() {
        return "Dirt";
    }

    @Override
    public boolean onActivate(Item item) {
        return this.onActivate(item, null);
    }

    @Override
    public boolean onActivate(Item item, ServerPlayer player) {
        if (item.isHoe()) {
            item.useOn(this);
            this.getLevel().setBlock(this, new BlockFarmland(), true);

            return true;
        }

        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}
