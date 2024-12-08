package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.level.Level;
import org.crimsonmc.level.Location;
import org.crimsonmc.level.Position;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.player.ServerPlayer;

public class PlayerTeleportEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private TeleportCause cause;

    private Location from;

    private Location to;

    private PlayerTeleportEvent(ServerPlayer player) {
        this.player = player;
    }

    public PlayerTeleportEvent(ServerPlayer player, Location from, Location to, TeleportCause cause) {
        this(player);
        this.from = from;
        this.to = to;
        this.cause = cause;
    }

    public PlayerTeleportEvent(ServerPlayer player, Vector3 from, Vector3 to, TeleportCause cause) {
        this(player);
        this.from = vectorToLocation(player.getLevel(), from);
        this.from = vectorToLocation(player.getLevel(), to);
        this.cause = cause;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public TeleportCause getCause() {
        return cause;
    }

    private Location vectorToLocation(Level baseLevel, Vector3 vector) {
        if (vector instanceof Location)
            return (Location) vector;
        if (vector instanceof Position)
            return ((Position) vector).getLocation();
        return new Location(vector.getX(), vector.getY(), vector.getZ(), 0, 0, baseLevel);
    }

    public enum TeleportCause {
        COMMAND,       // For crimsonmc tp command only
        PLUGIN,        // Every plugin
        NETHER_PORTAL, // Teleport using Nether portal
        UNKNOWN        // Unknown cause
    }
}
