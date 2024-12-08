package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemBone extends Item {

    public ItemBone() {
        this(0, 1);
    }

    public ItemBone(Integer meta) {
        this(meta, 1);
    }

    public ItemBone(Integer meta, int count) {
        super(BONE, meta, count, "Bone");
    }
}