package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockPackedIce extends BlockIce {

    public BlockPackedIce() {
        this(0);
    }

    public BlockPackedIce(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return PACKED_ICE;
    }

    @Override
    public String getName() {
        return "Packed Ice";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int onUpdate(int type) {
        return 0; // not being melted
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{this.getId(), 0, 1}};
    }
}
