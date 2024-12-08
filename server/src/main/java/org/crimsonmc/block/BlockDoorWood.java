package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockDoorWood extends BlockDoor {

    public BlockDoorWood() {
        this(0);
    }

    public BlockDoorWood(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Wood Door Block";
    }

    @Override
    public int getId() {
        return WOOD_DOOR_BLOCK;
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
        return ItemTool.TYPE_AXE;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{Item.WOODEN_DOOR, 0, 1}};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }
}
