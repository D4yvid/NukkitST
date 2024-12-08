package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

import java.util.Random;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockGravel extends BlockFallable {

    public BlockGravel() {
        this(0);
    }

    public BlockGravel(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return GRAVEL;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHOVEL;
    }

    @Override
    public String getName() {
        return "Gravel";
    }

    @Override
    public int[][] getDrops(Item item) {
        if (new Random().nextInt(9) == 0) {
            return new int[][]{{Item.FLINT, 0, 1}};
        } else {
            return new int[][]{{Item.GRAVEL, 0, 1}};
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SAND_BLOCK_COLOR;
    }
}
