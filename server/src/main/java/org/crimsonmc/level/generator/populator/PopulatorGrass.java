package org.crimsonmc.level.generator.populator;

import org.crimsonmc.block.Block;
import org.crimsonmc.level.ChunkManager;
import org.crimsonmc.math.MathUtilities;
import org.crimsonmc.math.RandomUtilities;

/**
 * author: Angelic47 crimsonmc Project
 */
public class PopulatorGrass extends Populator {

    private ChunkManager level;

    private int randomAmount;

    private int baseAmount;

    public void setRandomAmount(int randomAmount) {
        this.randomAmount = randomAmount;
    }

    public void setBaseAmount(int baseAmount) {
        this.baseAmount = baseAmount;
    }

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, RandomUtilities random) {
        this.level = level;
        int amount = random.nextBoundedInt(this.randomAmount + 1) + this.baseAmount;
        for (int i = 0; i < amount; ++i) {
            int x = MathUtilities.randomRange(random, chunkX * 16, chunkX * 16 + 15);
            int z = MathUtilities.randomRange(random, chunkZ * 16, chunkZ * 16 + 15);
            int y = this.getHighestWorkableBlock(x, z);

            if (y != -1 && this.canGrassStay(x, y, z)) {
                this.level.setBlockIdAt(x, y, z, Block.TALL_GRASS);
                this.level.setBlockDataAt(x, y, z, 0);
            }
        }
    }

    private boolean canGrassStay(int x, int y, int z) {
        int b = this.level.getBlockIdAt(x, y, z);
        return (b == Block.AIR || b == Block.SNOW_LAYER) &&
                this.level.getBlockIdAt(x, y - 1, z) == Block.GRASS;
    }

    private int getHighestWorkableBlock(int x, int z) {
        int y;
        for (y = 127; y >= 0; --y) {
            int b = this.level.getBlockIdAt(x, y, z);
            if (b != Block.AIR && b != Block.LEAVES && b != Block.LEAVES2 && b != Block.SNOW_LAYER) {
                break;
            }
        }

        return y == 0 ? -1 : ++y;
    }
}
