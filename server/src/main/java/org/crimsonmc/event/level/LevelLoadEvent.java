package org.crimsonmc.event.level;

import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.Level;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class LevelLoadEvent extends LevelEvent {

    private static final HandlerList handlers = new HandlerList();

    public LevelLoadEvent(Level level) {
        super(level);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
