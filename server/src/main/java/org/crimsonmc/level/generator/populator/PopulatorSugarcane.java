package org.crimsonmc.level.generator.populator;

import org.crimsonmc.block.Block;
import org.crimsonmc.level.ChunkManager;
import org.crimsonmc.math.MathUtilities;
import org.crimsonmc.math.RandomUtilities;

/**
 * crimsonmc Minecraft PE Server Software This class was written by Niall Lindsay <Niall7459>
 **/

public class PopulatorSugarcane extends Populator {

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

            if (y != -1 && this.canSugarcaneStay(x, y, z)) {
                this.level.setBlockIdAt(x, y, z, Block.SUGARCANE_BLOCK);
                this.level.setBlockDataAt(x, y, z, 1);
            }
        }
    }

    private boolean findWater(int x, int y, int z) {
        int count = 0;
        for (int i = x - 4; i < (x + 4); i++) {
            for (int j = z - 4; j < (z + 4); j++) {
                int b = this.level.getBlockIdAt(i, y, j);
                if (b == Block.WATER || b == Block.STILL_WATER) {
                    count++;
                }
                if (count > 10) {
                    return true;
                }
            }
        }
        return (count > 10);
    }

    private boolean canSugarcaneStay(int x, int y, int z) {
        int b = this.level.getBlockIdAt(x, y, z);
        int c = this.level.getBlockIdAt(x, y - 1, z);
        return (b == Block.AIR) && (c == Block.SAND || c == Block.GRASS) && this.findWater(x, y - 1, z);
    }

    private int getHighestWorkableBlock(int x, int z) {
        int y;
        for (y = 127; y >= 0; --y) {
            int b = this.level.getBlockIdAt(x, y, z);
            if (b != Block.AIR && b != Block.LEAVES && b != Block.LEAVES2) {
                break;
            }
        }

        return y == 0 ? -1 : ++y;
    }
}
