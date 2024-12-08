package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.math.RandomUtilities;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockOreCoal extends BlockSolid {

    public BlockOreCoal() {
        this(0);
    }

    public BlockOreCoal(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return COAL_ORE;
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
        return "Coal Ore";
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{{Item.COAL, 0, 1}};
        } else {
            return new int[0][0];
        }
    }

    @Override
    public int getDropExp() {
        return new RandomUtilities().nextRange(0, 2);
    }
}
