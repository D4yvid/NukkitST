package org.crimsonmc.item;

import org.crimsonmc.block.BlockStemPumpkin;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemSeedsPumpkin extends Item {

    public ItemSeedsPumpkin() {
        this(0, 1);
    }

    public ItemSeedsPumpkin(Integer meta) {
        this(meta, 1);
    }

    public ItemSeedsPumpkin(Integer meta, int count) {
        super(PUMPKIN_SEEDS, 0, count, "Pumpkin Seeds");
        this.block = new BlockStemPumpkin();
    }
}
