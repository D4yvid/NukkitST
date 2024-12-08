package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.Location;
import org.crimsonmc.player.ServerPlayer;

public class PlayerMoveEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Location from;

    private Location to;

    public PlayerMoveEvent(ServerPlayer player, Location from, Location to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }
}
