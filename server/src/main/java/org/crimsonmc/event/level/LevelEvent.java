package org.crimsonmc.event.level;

import org.crimsonmc.event.Event;
import org.crimsonmc.level.Level;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class LevelEvent extends Event {

    private final Level level;

    public LevelEvent(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
