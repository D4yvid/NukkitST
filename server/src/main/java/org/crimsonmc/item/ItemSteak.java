package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemSteak extends ItemEdible {

    public ItemSteak() {
        this(0, 1);
    }

    public ItemSteak(Integer meta) {
        this(meta, 1);
    }

    public ItemSteak(Integer meta, int count) {
        super(STEAK, meta, count, "Steak");
    }
}
