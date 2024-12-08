package org.crimsonmc.level.generator.biome;

import org.crimsonmc.block.Block;
import org.crimsonmc.block.BlockSand;
import org.crimsonmc.block.BlockSandstone;
import org.crimsonmc.level.generator.populator.PopulatorCactus;
import org.crimsonmc.level.generator.populator.PopulatorDeadBush;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class SandyBiome extends NormalBiome {

    public SandyBiome() {

        PopulatorCactus cactus = new PopulatorCactus();
        cactus.setBaseAmount(2);

        PopulatorDeadBush deadbush = new PopulatorDeadBush();
        deadbush.setBaseAmount(2);

        this.addPopulator(cactus);
        this.addPopulator(deadbush);

        this.setGroundCover(new Block[]{new BlockSand(), new BlockSand(), new BlockSandstone(),
                new BlockSandstone(), new BlockSandstone()});
    }
}
