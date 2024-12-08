package org.crimsonmc.item;

import org.crimsonmc.block.BlockSignPost;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemSign extends Item {

    public ItemSign() {
        this(0, 1);
    }

    public ItemSign(Integer meta) {
        this(meta, 1);
    }

    public ItemSign(Integer meta, int count) {
        super(SIGN, 0, count, "Sign");
        this.block = new BlockSignPost();
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}
