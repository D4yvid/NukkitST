package org.crimsonmc.event.player;

import org.crimsonmc.event.Event;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class PlayerEvent extends Event {

    protected ServerPlayer player;

    public ServerPlayer getPlayer() {
        return player;
    }
}
