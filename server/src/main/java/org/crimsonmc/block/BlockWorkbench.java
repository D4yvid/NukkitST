package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created on 2015/12/5 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockWorkbench extends BlockSolid {

    public BlockWorkbench() {
        this(0);
    }

    public BlockWorkbench(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Crafting Table";
    }

    @Override
    public int getId() {
        return WORKBENCH;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 2.5;
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
    public boolean onActivate(Item item, ServerPlayer player) {
        if (player != null) {
            player.craftingType = ServerPlayer.CRAFTING_BIG;
        }
        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{this.getId(), 0, 1}};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }
}
