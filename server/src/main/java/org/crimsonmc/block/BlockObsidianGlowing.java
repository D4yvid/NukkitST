package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;

/**
 * Created on 2015/11/22 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockObsidianGlowing extends BlockSolid {

    public BlockObsidianGlowing() {
        this(0);
    }

    public BlockObsidianGlowing(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return GLOWING_OBSIDIAN;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Glowing Obsidian";
    }

    @Override
    public double getResistance() {
        return 6000;
    }

    @Override
    public int getLightLevel() {
        return 12;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() > ItemTool.DIAMOND_PICKAXE) {
            return new int[][]{{Item.GLOWING_OBSIDIAN, 0, 1}};
        } else {
            return new int[0][0];
        }
    }
}
