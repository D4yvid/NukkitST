package org.crimsonmc.level.generator.biome;

import org.crimsonmc.block.Block;
import org.crimsonmc.block.BlockDirt;

/**
 * author: Angelic47 crimsonmc Project
 */
public abstract class WateryBiome extends NormalBiome {

    public WateryBiome() {
        this.setGroundCover(new Block[]{new BlockDirt(), new BlockDirt(), new BlockDirt(),
                new BlockDirt(), new BlockDirt()});
    }
}
