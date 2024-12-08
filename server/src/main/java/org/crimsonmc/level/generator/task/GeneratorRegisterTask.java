package org.crimsonmc.level.generator.task;

import org.crimsonmc.block.Block;
import org.crimsonmc.level.Level;
import org.crimsonmc.level.SimpleChunkManager;
import org.crimsonmc.level.generator.Generator;
import org.crimsonmc.level.generator.biome.Biome;
import org.crimsonmc.math.RandomUtilities;
import org.crimsonmc.scheduler.AsyncTask;

import java.util.Map;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class GeneratorRegisterTask extends AsyncTask {

    public final Class<? extends Generator> generator;

    public final Map<String, Object> settings;

    public final long seed;

    public final int levelId;

    public GeneratorRegisterTask(Level level, Generator generator) {
        this.generator = generator.getClass();
        this.settings = generator.getSettings();
        this.seed = level.getSeed();
        this.levelId = level.getId();
    }

    @Override
    public void onRun() {
        Block.init();
        Biome.init();
        SimpleChunkManager manager = new SimpleChunkManager(this.seed);
        try {
            Generator generator = this.generator.getConstructor(Map.class).newInstance(this.settings);
            generator.init(manager, new RandomUtilities(manager.getSeed()));
            GeneratorPool.put(this.levelId, generator);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
