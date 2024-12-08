package org.crimsonmc.event.inventory;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.inventory.Inventory;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: Box crimsonmc Project
 */
public class InventoryOpenEvent extends InventoryEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final ServerPlayer who;

    public InventoryOpenEvent(Inventory inventory, ServerPlayer who) {
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