package org.crimsonmc.item;

import org.crimsonmc.block.BlockBrewingStand;

public class ItemBrewingStand extends Item {

    public ItemBrewingStand(Integer meta) {
        this(meta, 1);
    }

    public ItemBrewingStand(Integer meta, int count) {
        super(BREWING_STAND, 0, count, "Brewing Stand");
        this.block = new BlockBrewingStand();
    }
}