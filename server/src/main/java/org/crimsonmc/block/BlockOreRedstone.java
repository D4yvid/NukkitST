package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.level.Level;
import org.crimsonmc.math.RandomUtilities;

import java.util.Random;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockOreRedstone extends BlockSolid {

    public BlockOreRedstone() {
        this(0);
    }

    public BlockOreRedstone(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return REDSTONE_ORE;
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Redstone Ore";
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_IRON) {
            return new int[][]{{Item.REDSTONE_DUST, 0, new Random().nextInt(1) + 4}};
        } else {
            return new int[0][0];
        }
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_TOUCH) { // type == Level.BLOCK_UPDATE_NORMAL ||
            this.getLevel().setBlock(this, new BlockOreRedstone(this.meta), false, false);

            return Level.BLOCK_UPDATE_WEAK;
        }

        return 0;
    }

    @Override
    public int getDropExp() {
        return new RandomUtilities().nextRange(1, 5);
    }
}
