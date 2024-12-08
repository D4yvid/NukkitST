package org.crimsonmc.inventory;

import org.crimsonmc.item.Item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public interface Transaction {

    Inventory getInventory();

    int getSlot();

    Item getSourceItem();

    Item getTargetItem();

    long getCreationTime();
}
