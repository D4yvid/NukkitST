package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.item.Item;
import org.crimsonmc.player.ServerPlayer;

public class PlayerDropItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Item drop;

    public PlayerDropItemEvent(ServerPlayer player, Item drop) {
        this.player = player;
        this.drop = drop;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Item getItem() {
        return this.drop;
    }
}
