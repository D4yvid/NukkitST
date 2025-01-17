package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemMelon extends ItemEdible {

    public ItemMelon() {
        this(0, 1);
    }

    public ItemMelon(Integer meta) {
        this(meta, 1);
    }

    public ItemMelon(Integer meta, int count) {
        super(MELON, meta, count, "Melon");
    }
}
