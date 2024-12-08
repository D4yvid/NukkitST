package org.crimsonmc.item;

import org.crimsonmc.block.BlockBed;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemBed extends Item {

    public ItemBed() {
        this(0, 1);
    }

    public ItemBed(Integer meta) {
        this(meta, 1);
    }

    public ItemBed(Integer meta, int count) {
        super(BED, 0, count, "BlockBed");
        this.block = new BlockBed();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
