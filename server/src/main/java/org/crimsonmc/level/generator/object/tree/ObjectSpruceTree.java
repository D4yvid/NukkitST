package org.crimsonmc.level.generator.object.tree;

import org.crimsonmc.block.Block;
import org.crimsonmc.block.BlockWood;
import org.crimsonmc.level.ChunkManager;
import org.crimsonmc.math.RandomUtilities;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ObjectSpruceTree extends ObjectTree {

    private int treeHeight = 15;

    @Override
    public int getTrunkBlock() {
        return Block.LOG;
    }

    @Override
    public int getLeafBlock() {
        return Block.LEAVES;
    }

    @Override
    public int getType() {
        return BlockWood.SPRUCE;
    }

    @Override
    public int getTreeHeight() {
        return this.treeHeight;
    }

    @Override
    public void placeObject(ChunkManager level, int x, int y, int z, RandomUtilities random) {
        this.treeHeight = random.nextBoundedInt(4) + 6;

        int topSize = this.getTreeHeight() - (1 + random.nextBoundedInt(2));
        int lRadius = 2 + random.nextBoundedInt(2);

        this.placeTrunk(level, x, y, z, random, this.getTreeHeight() - random.nextBoundedInt(3));

        int radius = random.nextBoundedInt(2);
        int maxR = 1;
        int minR = 0;

        for (int yy = 0; yy <= topSize; ++yy) {
            int yyy = y + this.treeHeight - yy;

            for (int xx = x - radius; xx <= x + radius; ++xx) {
                int xOff = Math.abs(xx - x);
                for (int zz = z - radius; zz <= z + radius; ++zz) {
                    int zOff = Math.abs(zz - z);
                    if (xOff == radius && zOff == radius && radius > 0) {
                        continue;
                    }

                    if (!Block.solid[level.getBlockIdAt(xx, yyy, zz)]) {
                        level.setBlockIdAt(xx, yyy, zz, this.getLeafBlock());
                        level.setBlockDataAt(xx, yyy, zz, this.getType());
                    }
                }
            }

            if (radius >= maxR) {
                radius = minR;
                minR = 1;
                if (++maxR > lRadius) {
                    maxR = lRadius;
                }
            } else {
                ++radius;
            }
        }
    }
}
