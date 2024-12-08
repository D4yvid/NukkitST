package org.crimsonmc.level.generator.populator;

import org.crimsonmc.level.ChunkManager;
import org.crimsonmc.math.RandomUtilities;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class Populator {

    public abstract void populate(ChunkManager level, int chunkX, int chunkZ, RandomUtilities random);
}
