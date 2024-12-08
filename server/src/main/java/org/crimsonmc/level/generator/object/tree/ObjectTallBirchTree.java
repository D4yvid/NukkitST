package org.crimsonmc.level.generator.object.tree;

import org.crimsonmc.level.ChunkManager;
import org.crimsonmc.math.RandomUtilities;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ObjectTallBirchTree extends ObjectBirchTree {

    @Override
    public void placeObject(ChunkManager level, int x, int y, int z, RandomUtilities random) {
        this.treeHeight = random.nextBoundedInt(3) + 10;
        super.placeObject(level, x, y, z, random);
    }
}
