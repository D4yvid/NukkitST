package org.crimsonmc.level.generator.biome;

import org.crimsonmc.block.Block;
import org.crimsonmc.block.BlockFlower;
import org.crimsonmc.block.BlockSapling;
import org.crimsonmc.level.generator.populator.PopulatorFlower;
import org.crimsonmc.level.generator.populator.PopulatorLilyPad;
import org.crimsonmc.level.generator.populator.PopulatorTree;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class SwampBiome extends GrassyBiome {

    public SwampBiome() {
        super();

        PopulatorLilyPad lilypad = new PopulatorLilyPad();
        lilypad.setBaseAmount(4);

        PopulatorTree trees = new PopulatorTree(BlockSapling.OAK);
        trees.setBaseAmount(2);

        PopulatorFlower flower = new PopulatorFlower();
        flower.setBaseAmount(2);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_BLUE_ORCHID);

        this.addPopulator(trees);
        this.addPopulator(flower);
        this.addPopulator(lilypad);

        this.setElevation(62, 63);

        this.temperature = 0.8;
        this.rainfall = 0.9;
    }

    @Override
    public String getName() {
        return "Swamp";
    }

    @Override
    public int getColor() {
        return 0x6a7039;
    }
}
