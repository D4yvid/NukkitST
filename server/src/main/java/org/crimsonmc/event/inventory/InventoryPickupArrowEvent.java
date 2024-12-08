package org.crimsonmc.event.inventory;

import org.crimsonmc.entity.projectile.EntityArrow;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.inventory.Inventory;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class InventoryPickupArrowEvent extends InventoryEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final EntityArrow arrow;

    public InventoryPickupArrowEvent(Inventory inventory, EntityArrow arrow) {
        super(inventory);
        this.arrow = arrow;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public EntityArrow getArrow() {
        return arrow;
    }
}