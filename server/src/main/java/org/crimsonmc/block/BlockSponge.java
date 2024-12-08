package org.crimsonmc.block;

/**
 * author: Angelic47 crimsonmc Project
 */
public class BlockSponge extends BlockSolid {

    public BlockSponge() {
        this(0);
    }

    public BlockSponge(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return SPONGE;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 3;
    }

    @Override
    public String getName() {
        return "Sponge";
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CLOTH_BLOCK_COLOR;
    }
}
