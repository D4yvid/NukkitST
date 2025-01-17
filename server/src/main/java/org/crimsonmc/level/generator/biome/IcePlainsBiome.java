package org.crimsonmc.level.generator.biome;

import org.crimsonmc.level.generator.populator.PopulatorTallGrass;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class IcePlainsBiome extends GrassyBiome {

    public IcePlainsBiome() {
        super();

        PopulatorTallGrass tallGrass = new PopulatorTallGrass();
        tallGrass.setBaseAmount(5);

        this.addPopulator(tallGrass);

        this.setElevation(63, 74);

        this.temperature = 0.05;
        this.rainfall = 0.8;
    }

    @Override
    public String getName() {
        return "Ice Plains";
    }
}
