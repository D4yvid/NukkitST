package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.level.Level;
import org.crimsonmc.redstone.Redstone;

/**
 * @author crimsonmc Project Team
 */
public class BlockRedstoneLamp extends BlockSolid {

    public BlockRedstoneLamp(int meta) {
        super(meta);
    }

    public BlockRedstoneLamp() {
        this(0);
    }

    @Override
    public String getName() {
        return "Redstone Lamp";
    }

    @Override
    public int getId() {
        return REDSTONE_LAMP;
    }

    @Override
    public double getHardness() {
        return 0.3D;
    }

    @Override
    public double getResistance() {
        return 1.5D;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int onUpdate(int type) {
        if ((type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_SCHEDULED) &&
                this.getNeighborPowerLevel() > 0) {
            int level = this.getPowerLevel();
            Redstone.deactive(this, level);
            this.getLevel().setBlock(this, new BlockLitRedstoneLamp());
        }
        return 0;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{Item.REDSTONE_LAMP, 0, 1}};
    }
}
