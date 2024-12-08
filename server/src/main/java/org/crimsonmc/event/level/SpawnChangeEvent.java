package org.crimsonmc.event.level;

import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.Level;
import org.crimsonmc.level.Position;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class SpawnChangeEvent extends LevelEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Position previousSpawn;

    public SpawnChangeEvent(Level level, Position previousSpawn) {
        super(level);
        this.previousSpawn = previousSpawn;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Position getPreviousSpawn() {
        return previousSpawn;
    }
}
