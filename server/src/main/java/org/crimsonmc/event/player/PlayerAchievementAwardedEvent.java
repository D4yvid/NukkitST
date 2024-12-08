package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.ServerPlayer;

public class PlayerAchievementAwardedEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected final String achievement;

    public PlayerAchievementAwardedEvent(ServerPlayer player, String achievementId) {
        this.player = player;
        this.achievement = achievementId;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public String getAchievement() {
        return this.achievement;
    }
}
