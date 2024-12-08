package org.crimsonmc.item;

/**
 * Created by Snake1999 on 2016/1/14. Package cn.crimsonmc.item in project crimsonmc.
 */
public class ItemClownfish extends ItemFish {

    public ItemClownfish() {
        this(0, 1);
    }

    public ItemClownfish(Integer meta) {
        this(meta, 1);
    }

    public ItemClownfish(Integer meta, int count) {
        super(CLOWNFISH, meta, count, "Clownfish");
    }
}
