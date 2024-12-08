package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.ServerPlayer;

public class PlayerAnimationEvent extends PlayerEvent implements Cancellable {

    public static final int ARM_SWING = 1;

    private static final HandlerList handlers = new HandlerList();

    private final int animationType;

    public PlayerAnimationEvent(ServerPlayer player) {
        this(player, ARM_SWING);
    }

    public PlayerAnimationEvent(ServerPlayer player, int animation) {
        this.player = player;
        this.animationType = animation;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public int getAnimationType() {
        return this.animationType;
    }
}
