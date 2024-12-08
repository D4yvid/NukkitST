package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.AdventureSettings;
import org.crimsonmc.player.ServerPlayer;

public class PlayerGameModeChangeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected final int gamemode;

    protected AdventureSettings newAdventureSettings;

    public PlayerGameModeChangeEvent(ServerPlayer player, int newGameMode,
                                     AdventureSettings newAdventureSettings) {
        this.player = player;
        this.gamemode = newGameMode;
        this.newAdventureSettings = newAdventureSettings;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public int getNewGamemode() {
        return gamemode;
    }

    public AdventureSettings getNewAdventureSettings() {
        return newAdventureSettings;
    }

    public void setNewAdventureSettings(AdventureSettings newAdventureSettings) {
        this.newAdventureSettings = newAdventureSettings;
    }
}
