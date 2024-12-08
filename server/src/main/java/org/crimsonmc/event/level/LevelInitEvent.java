package org.crimsonmc.event.level;

import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.Level;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class LevelInitEvent extends LevelEvent {

    private static final HandlerList handlers = new HandlerList();

    public LevelInitEvent(Level level) {
        super(level);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
