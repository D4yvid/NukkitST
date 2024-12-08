package org.crimsonmc.level.generator.task;

import org.crimsonmc.level.Level;
import org.crimsonmc.scheduler.AsyncTask;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class GeneratorUnregisterTask extends AsyncTask {

    public final int levelId;

    public GeneratorUnregisterTask(Level level) {
        this.levelId = level.getId();
    }

    @Override
    public void onRun() {
        GeneratorPool.remove(levelId);
    }
}
