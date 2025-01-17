package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

/**
 * Created on 2015/12/2 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockMossStone extends BlockSolid {

    public BlockMossStone() {
        this(0);
    }

    public BlockMossStone(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Moss Stone";
    }

    @Override
    public int getId() {
        return MOSS_STONE;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 10;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{{Item.MOSS_STONE, this.meta, 1}};
        } else {
            return new int[0][0];
        }
    }
}
