package org.crimsonmc.block;

import org.crimsonmc.item.Item;

/**
 * Created on 2015/11/22 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockBeetroot extends BlockCrops {

    public BlockBeetroot() {
        this(0);
    }

    public BlockBeetroot(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BEETROOT_BLOCK;
    }

    @Override
    public String getName() {
        return "Beetroot Block";
    }

    @Override
    public int[][] getDrops(Item item) {
        if (this.meta >= 0x07) {
            return new int[][]{{Item.BEETROOT, 0, 1},
                    {Item.BEETROOT_SEEDS, 0, (int) (4d * Math.random())}};
        } else {
            return new int[][]{{Item.BEETROOT_SEEDS, 0, 1}};
        }
    }
}
