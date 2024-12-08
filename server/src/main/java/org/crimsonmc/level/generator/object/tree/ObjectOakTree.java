package org.crimsonmc.level.generator.object.tree;

import org.crimsonmc.block.Block;
import org.crimsonmc.block.BlockWood;
import org.crimsonmc.level.ChunkManager;
import org.crimsonmc.math.RandomUtilities;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ObjectOakTree extends ObjectTree {

    private int treeHeight = 7;

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
        return BlockWood.OAK;
    }

    @Override
    public int getTreeHeight() {
        return this.treeHeight;
    }

    @Override
    public void placeObject(ChunkManager level, int x, int y, int z, RandomUtilities random) {
        this.treeHeight = random.nextBoundedInt(3) + 4;
        super.placeObject(level, x, y, z, random);
    }
}
