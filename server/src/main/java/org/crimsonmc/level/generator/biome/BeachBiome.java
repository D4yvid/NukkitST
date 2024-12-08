package org.crimsonmc.level.generator.biome;

import org.crimsonmc.block.Block;
import org.crimsonmc.block.BlockSand;
import org.crimsonmc.block.BlockSandstone;

/**
 * Author: PeratX crimsonmc Project
 */
public class BeachBiome extends SandyBiome {

    public BeachBiome() {
        // Todo: SugarCane

        this.setElevation(62, 65);
        this.temperature = 2;
        this.rainfall = 0;

        this.setGroundCover(new Block[]{new BlockSand(), new BlockSand(), new BlockSandstone(),
                new BlockSandstone(), new BlockSandstone()});
    }

    @Override
    public String getName() {
        return "Beach";
    }
}
