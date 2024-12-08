package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemWheat extends Item {

    public ItemWheat() {
        this(0, 1);
    }

    public ItemWheat(Integer meta) {
        this(meta, 1);
    }

    public ItemWheat(Integer meta, int count) {
        super(WHEAT, meta, count, "Wheat");
    }
}
