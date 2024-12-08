package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemLeather extends Item {

    public ItemLeather() {
        this(0, 1);
    }

    public ItemLeather(Integer meta) {
        this(meta, 1);
    }

    public ItemLeather(Integer meta, int count) {
        super(LEATHER, meta, count, "Leather");
    }
}
