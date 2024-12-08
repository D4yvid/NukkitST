package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.item.Item;
import org.crimsonmc.player.ServerPlayer;

/**
 * Called when a player eats something
 */
public class PlayerItemConsumeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Item item;

    public PlayerItemConsumeEvent(ServerPlayer player, Item item) {
        this.player = player;
        this.item = item;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Item getItem() {
        return this.item.clone();
    }
}
