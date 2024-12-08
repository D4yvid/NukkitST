package org.crimsonmc.event.level;

import org.crimsonmc.event.Event;
import org.crimsonmc.level.Level;

/**
 * author: funcraft crimsonmc Project
 */
public abstract class WeatherEvent extends Event {

    private final Level level;

    public WeatherEvent(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
