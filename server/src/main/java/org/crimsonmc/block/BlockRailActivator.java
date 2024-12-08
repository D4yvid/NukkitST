package org.crimsonmc.block;

import org.crimsonmc.item.Item;

/**
 * @author crimsonmc Project Team
 */
public class BlockRailActivator extends BlockRail {

    public BlockRailActivator(int meta) {
        super(meta);
    }

    public BlockRailActivator() {
        this(0);
    }

    @Override
    public String getName() {
        return "Activator Rail";
    }

    @Override
    public int getId() {
        return ACTIVATOR_RAIL;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{Item.ACTIVATOR_RAIL, 0, 1}};
    }
}
