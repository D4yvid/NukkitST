package org.crimsonmc.event.inventory;

import org.crimsonmc.event.Event;
import org.crimsonmc.inventory.Inventory;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class InventoryEvent extends Event {

    protected final Inventory inventory;

    public InventoryEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ServerPlayer[] getViewers() {
        return this.inventory.getViewers().stream().toArray(ServerPlayer[]::new);
    }
}
