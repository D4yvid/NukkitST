package org.crimsonmc.level.generator.populator;

import org.crimsonmc.level.ChunkManager;
import org.crimsonmc.level.generator.object.ore.ObjectOre;
import org.crimsonmc.level.generator.object.ore.OreType;
import org.crimsonmc.math.MathUtilities;
import org.crimsonmc.math.RandomUtilities;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class PopulatorOre extends Populator {

    private OreType[] oreTypes = new OreType[0];

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, RandomUtilities random) {
        for (OreType type : this.oreTypes) {
            ObjectOre ore = new ObjectOre(random, type);
            for (int i = 0; i < ore.type.clusterCount; ++i) {
                int x = MathUtilities.randomRange(random, chunkX << 4, (chunkX << 4) + 15);
                int y = MathUtilities.randomRange(random, ore.type.minHeight, ore.type.maxHeight);
                int z = MathUtilities.randomRange(random, chunkZ << 4, (chunkZ << 4) + 15);
                if (ore.canPlaceObject(level, x, y, z)) {
                    ore.placeObject(level, x, y, z);
                }
            }
        }
    }

    public void setOreTypes(OreType[] oreTypes) {
        this.oreTypes = oreTypes;
    }
}
