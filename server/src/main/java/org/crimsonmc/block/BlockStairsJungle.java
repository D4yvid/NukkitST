package org.crimsonmc.block;

/**
 * Created on 2015/11/25 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockStairsJungle extends BlockStairsWood {

    public BlockStairsJungle() {
        this(0);
    }

    public BlockStairsJungle(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return JUNGLE_WOOD_STAIRS;
    }

    @Override
    public String getName() {
        return "Jungle Wood Stairs";
    }
}
