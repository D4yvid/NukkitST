package org.crimsonmc.level.generator.biome;

import org.crimsonmc.block.Block;
import org.crimsonmc.level.generator.populator.PopulatorSugarcane;
import org.crimsonmc.level.generator.populator.PopulatorTallSugarcane;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class OceanBiome extends WateryBiome {

    public OceanBiome() {
        super();

        PopulatorSugarcane sugarcane = new PopulatorSugarcane();
        sugarcane.setBaseAmount(6);
        PopulatorTallSugarcane tallSugarcane = new PopulatorTallSugarcane();
        tallSugarcane.setBaseAmount(60);
        this.addPopulator(sugarcane);
        this.addPopulator(tallSugarcane);
        this.setElevation(46, 58);

        this.temperature = 0.5;
        this.rainfall = 0.5;
    }

    @Override
    public Block[] getGroundCover() {
        return super.getGroundCover();
    }

    @Override
    public String getName() {
        return "Ocean";
    }
}
