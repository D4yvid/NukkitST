package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemBeefRaw extends ItemEdible {

    public ItemBeefRaw() {
        this(0, 1);
    }

    public ItemBeefRaw(Integer meta) {
        this(meta, 1);
    }

    public ItemBeefRaw(Integer meta, int count) {
        super(RAW_BEEF, meta, count, "Raw Beef");
    }
}
