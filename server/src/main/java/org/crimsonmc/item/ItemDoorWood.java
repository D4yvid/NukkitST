package org.crimsonmc.item;

import org.crimsonmc.block.BlockDoorWood;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemDoorWood extends Item {

    public ItemDoorWood() {
        this(0, 1);
    }

    public ItemDoorWood(Integer meta) {
        this(meta, 1);
    }

    public ItemDoorWood(Integer meta, int count) {
        super(WOODEN_DOOR, 0, count, "Oak Door");
        this.block = new BlockDoorWood();
    }
}
