package org.crimsonmc.level.generator.task;

import org.crimsonmc.level.Level;
import org.crimsonmc.level.format.generic.BaseFullChunk;
import org.crimsonmc.scheduler.AsyncTask;
import org.crimsonmc.server.Server;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class LightPopulationTask extends AsyncTask {

    public final int levelId;

    public BaseFullChunk chunk;

    public LightPopulationTask(Level level, BaseFullChunk chunk) {
        this.levelId = level.getId();
        this.chunk = chunk;
    }

    @Override
    public void onRun() {
        BaseFullChunk chunk = this.chunk.clone();
        if (chunk == null) {
            return;
        }

        chunk.recalculateHeightMap();
        chunk.populateSkyLight();
        chunk.setLightPopulated();

        this.chunk = chunk.clone();
    }

    @Override
    public void onCompletion(Server server) {
        Level level = server.getLevel(this.levelId);

        BaseFullChunk chunk = this.chunk.clone();
        if (level != null) {
            if (chunk == null) {
                return;
            }

            level.generateChunkCallback(chunk.getX(), chunk.getZ(), chunk);
        }
    }
}
