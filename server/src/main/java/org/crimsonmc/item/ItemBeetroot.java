package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemBeetroot extends ItemEdible {

    public ItemBeetroot() {
        this(0, 1);
    }

    public ItemBeetroot(Integer meta) {
        this(meta, 1);
    }

    public ItemBeetroot(Integer meta, int count) {
        super(BEETROOT, meta, count, "Beetroot");
    }
}