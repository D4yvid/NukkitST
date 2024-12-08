package org.crimsonmc.item;

import org.crimsonmc.block.BlockDoorIron;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemDoorIron extends Item {

    public ItemDoorIron() {
        this(0, 1);
    }

    public ItemDoorIron(Integer meta) {
        this(meta, 1);
    }

    public ItemDoorIron(Integer meta, int count) {
        super(IRON_DOOR, 0, count, "Iron Door");
        this.block = new BlockDoorIron();
    }
}
