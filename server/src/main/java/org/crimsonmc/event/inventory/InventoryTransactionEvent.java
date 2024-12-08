package org.crimsonmc.event.inventory;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.Event;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.inventory.TransactionGroup;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class InventoryTransactionEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final TransactionGroup transaction;

    public InventoryTransactionEvent(TransactionGroup transaction) {
        this.transaction = transaction;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public TransactionGroup getTransaction() {
        return transaction;
    }
}