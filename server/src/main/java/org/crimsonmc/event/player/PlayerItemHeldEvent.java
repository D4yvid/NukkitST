package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.item.Item;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class PlayerItemHeldEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Item item;

    private final int slot;

    private final int inventorySlot;

    public PlayerItemHeldEvent(ServerPlayer player, Item item, int inventorySlot, int slot) {
        this.player = player;
        this.item = item;
        this.inventorySlot = inventorySlot;
        this.slot = slot;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public int getSlot() {
        return slot;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public Item getItem() {
        return item;
    }
}
