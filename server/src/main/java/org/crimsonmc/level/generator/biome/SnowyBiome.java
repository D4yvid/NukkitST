package org.crimsonmc.level.generator.biome;

import org.crimsonmc.block.Block;
import org.crimsonmc.block.BlockDirt;
import org.crimsonmc.block.BlockGrass;
import org.crimsonmc.block.BlockSnowLayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class SnowyBiome extends NormalBiome {

    public SnowyBiome() {
        this.setGroundCover(new Block[]{new BlockSnowLayer(), new BlockGrass(), new BlockDirt(),
                new BlockDirt(), new BlockDirt()});
    }
}
