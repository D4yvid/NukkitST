package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

/**
 * @author crimsonmc Project Team
 */
public class BlockBricks extends BlockSolid {

    public BlockBricks(int meta) {
        super(meta);
    }

    public BlockBricks() {
        this(0);
    }

    @Override
    public String getName() {
        return "Bricks";
    }

    @Override
    public int getId() {
        return BRICKS_BLOCK;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{{Item.BRICKS_BLOCK, 0, 1}};
        } else {
            return new int[0][0];
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.STONE_BLOCK_COLOR;
    }
}
