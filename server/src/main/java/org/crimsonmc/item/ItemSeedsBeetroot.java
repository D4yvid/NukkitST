package org.crimsonmc.item;

import org.crimsonmc.block.BlockBeetroot;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemSeedsBeetroot extends Item {

    public ItemSeedsBeetroot() {
        this(0, 1);
    }

    public ItemSeedsBeetroot(Integer meta) {
        this(meta, 1);
    }

    public ItemSeedsBeetroot(Integer meta, int count) {
        super(BEETROOT_SEEDS, 0, count, "Beetroot Seeds");
        this.block = new BlockBeetroot();
    }
}
