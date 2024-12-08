package org.crimsonmc.event.inventory;

import org.crimsonmc.entity.item.EntityItem;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.inventory.Inventory;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class InventoryPickupItemEvent extends InventoryEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final EntityItem item;

    public InventoryPickupItemEvent(Inventory inventory, EntityItem item) {
        super(inventory);
        this.item = item;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public EntityItem getItem() {
        return item;
    }
}