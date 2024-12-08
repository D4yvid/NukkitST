package org.crimsonmc.block;

import org.crimsonmc.item.Item;

/**
 * author: Angelic47 crimsonmc Project
 */
public class BlockBedrock extends BlockSolid {

    public BlockBedrock() {
        this(0);
    }

    public BlockBedrock(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BEDROCK;
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public double getResistance() {
        return 18000000;
    }

    @Override
    public String getName() {
        return "Bedrock";
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }
}
