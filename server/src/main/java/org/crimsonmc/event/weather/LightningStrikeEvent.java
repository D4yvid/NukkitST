package org.crimsonmc.event.weather;

import org.crimsonmc.entity.weather.EntityLightningStrike;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.Level;

/**
 * author: funcraft crimsonmc Project
 */
public class LightningStrikeEvent extends WeatherEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final EntityLightningStrike bolt;

    public LightningStrikeEvent(Level level, final EntityLightningStrike bolt) {
        super(level);
        this.bolt = bolt;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    /**
     * * Gets the bolt which is striking the earth. * @return lightning entity
     */
    public EntityLightningStrike getLightning() {
        return bolt;
    }
}
