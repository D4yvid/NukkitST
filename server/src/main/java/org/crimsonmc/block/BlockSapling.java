package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.level.Level;
import org.crimsonmc.level.generator.object.tree.ObjectTree;
import org.crimsonmc.math.RandomUtilities;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: Angelic47 crimsonmc Project
 */
public class BlockSapling extends BlockFlowable {

    public static final int OAK = 0;

    public static final int SPRUCE = 1;

    public static final int BIRCH = 2;

    public static final int JUNGLE = 3;

    public static final int ACACIA = 4;

    public static final int DARK_OAK = 5;

    public BlockSapling() {
        this(0);
    }

    public BlockSapling(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return SAPLING;
    }

    @Override
    public String getName() {
        String[] names = new String[]{"Oak Sapling",
                "Spruce Sapling",
                "Birch Sapling",
                "Jungle Sapling",
                "Acacia Sapling",
                "Dark Oak Sapling",
                "",
                ""};
        return names[this.meta & 0x07];
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        Block down = this.getSide(Block.SIDE_DOWN);
        if (down.getId() == Block.GRASS || down.getId() == Block.DIRT ||
                down.getId() == Block.FARMLAND || down.getId() == Block.PODZOL) {
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }

        return false;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    public boolean onActivate(Item item, ServerPlayer player) {
        if (item.getId() == Item.DYE && item.getDamage() == 0x0F) { // BoneMeal
            ObjectTree.growTree(this.getLevel(), (int) this.x, (int) this.y, (int) this.z,
                    new RandomUtilities(), this.meta & 0x07);
            if ((player.gamemode & 0x01) == 0) {
                item.count--;
            }

            return true;
        }
        this.getLevel().loadChunk((int) this.x >> 4, (int) this.z >> 4);
        return false;
    }

    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.getSide(0).isTransparent()) {
                this.getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) { // Growth
            if (new RandomUtilities().nextRange(1, 7) == 1) {
                if ((this.meta & 0x08) == 0x08) {
                    ObjectTree.growTree(this.getLevel(), (int) this.x, (int) this.y, (int) this.z,
                            new RandomUtilities(), this.meta & 0x07);
                } else {
                    this.meta |= 0x08;
                    this.getLevel().setBlock(this, this, true);
                    return Level.BLOCK_UPDATE_RANDOM;
                }
            } else {
                return Level.BLOCK_UPDATE_RANDOM;
            }
        }
        return 1;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{Item.SAPLING, this.getDamage(), 1}};
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}
