package org.crimsonmc.event.level;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.Level;

/**
 * author: funcraft crimsonmc Project
 */
public class WeatherChangeEvent extends WeatherEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final boolean to;

    public WeatherChangeEvent(Level level, boolean to) {
        super(level);
        this.to = to;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets the state of weather that the world is being set to
     *
     * @return true if the weather is being set to raining, false otherwise
     */
    public boolean toWeatherState() {
        return to;
    }
}
