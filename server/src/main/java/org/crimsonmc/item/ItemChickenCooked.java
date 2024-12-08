package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemChickenCooked extends ItemEdible {

    public ItemChickenCooked() {
        this(0, 1);
    }

    public ItemChickenCooked(Integer meta) {
        this(meta, 1);
    }

    public ItemChickenCooked(Integer meta, int count) {
        super(COOKED_CHICKEN, meta, count, "Cooked Chicken");
    }
}
