package org.crimsonmc.item;

import org.crimsonmc.block.BlockDoorDarkOak;

public class ItemDoorDarkOak extends Item {

    public ItemDoorDarkOak() {
        this(0, 1);
    }

    public ItemDoorDarkOak(Integer meta) {
        this(meta, 1);
    }

    public ItemDoorDarkOak(Integer meta, int count) {
        super(DARK_OAK_DOOR, 0, count, "Dark Oak Door");
        this.block = new BlockDoorDarkOak();
    }
}
