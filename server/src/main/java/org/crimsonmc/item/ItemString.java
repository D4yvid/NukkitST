package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemString extends Item {

    public ItemString() {
        this(0, 1);
    }

    public ItemString(Integer meta) {
        this(meta, 1);
    }

    public ItemString(Integer meta, int count) {
        super(STRING, meta, count, "String");
    }
}
