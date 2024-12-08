package org.crimsonmc.inventory;

import org.crimsonmc.item.Item;

import java.util.UUID;

/**
 * author: MagicDroidX crimsonmc Project
 */
public interface Recipe {

    Item getResult();

    void registerToCraftingManager();

    UUID getId();

    void setId(UUID id);
}
