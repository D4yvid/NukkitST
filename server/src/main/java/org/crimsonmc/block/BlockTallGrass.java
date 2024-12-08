package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.level.Level;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.player.ServerPlayer;

import java.util.Random;

/**
 * author: Angelic47 crimsonmc Project
 */
public class BlockTallGrass extends BlockFlowable {

    public BlockTallGrass() {
        this(1);
    }

    public BlockTallGrass(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return TALL_GRASS;
    }

    @Override
    public String getName() {
        String[] names = new String[]{"Dead Shrub", "Tall Grass", "Fern", ""};
        return names[this.meta & 0x03];
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean canBeReplaced() {
        return true;
    }

    @Override
    public int getBurnChance() {
        return 60;
    }

    @Override
    public int getBurnAbility() {
        return 100;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        Block down = this.getSide(Vector3.SIDE_DOWN);
        if (down.getId() == Block.GRASS || down.getId() == Block.DIRT || down.getId() == Block.PODZOL) {
            this.getLevel().setBlock(block, this, true);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.getSide(0).isTransparent()) {
                this.getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public boolean onActivate(Item item) {
        return this.onActivate(item, null);
    }

    @Override
    public boolean onActivate(Item item, ServerPlayer player) {
        // todo bonemeal

        return false;
    }

    @Override
    public int[][] getDrops(Item item) {
        boolean dropSeeds = new Random().nextInt(10) == 0;
        if (item.isShears()) {
            // todo enchantment
            if (dropSeeds) {
                return new int[][]{{Item.SEEDS, 0, 1}, {Item.TALL_GRASS, this.meta, 1}};
            } else {
                return new int[][]{{Item.TALL_GRASS, this.meta, 1}};
            }
        }

        if (dropSeeds) {
            return new int[][]{
                    {Item.SEEDS, 0, 1},
            };
        } else {
            return new int[0][0];
        }
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHEARS;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}
