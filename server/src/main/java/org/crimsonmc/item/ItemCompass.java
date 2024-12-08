package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemCompass extends Item {

    public ItemCompass() {
        this(0, 1);
    }

    public ItemCompass(Integer meta) {
        this(meta, 1);
    }

    public ItemCompass(Integer meta, int count) {
        super(COMPASS, meta, count, "Compass");
    }
}
