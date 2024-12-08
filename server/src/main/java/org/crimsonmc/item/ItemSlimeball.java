package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemSlimeball extends Item {

    public ItemSlimeball() {
        this(0, 1);
    }

    public ItemSlimeball(Integer meta) {
        this(meta, 1);
    }

    public ItemSlimeball(Integer meta, int count) {
        super(SLIMEBALL, meta, count, "Slimeball");
    }
}
