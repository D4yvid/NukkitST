package org.crimsonmc.level.generator.biome;

import org.crimsonmc.block.Block;
import org.crimsonmc.block.BlockDirt;
import org.crimsonmc.block.BlockGrass;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class GrassyBiome extends NormalBiome {

    public GrassyBiome() {
        this.setGroundCover(new Block[]{new BlockGrass(), new BlockDirt(), new BlockDirt(),
                new BlockDirt(), new BlockDirt()});
    }
}
