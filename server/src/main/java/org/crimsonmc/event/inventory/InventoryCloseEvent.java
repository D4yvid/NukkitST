package org.crimsonmc.event.inventory;

import org.crimsonmc.event.HandlerList;
import org.crimsonmc.inventory.Inventory;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: Box crimsonmc Project
 */
public class InventoryCloseEvent extends InventoryEvent {

    private static final HandlerList handlers = new HandlerList();

    private final ServerPlayer who;

    public InventoryCloseEvent(Inventory inventory, ServerPlayer who) {
        super(inventory);
        this.who = who;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public ServerPlayer getPlayer() {
        return this.who;
    }
}
