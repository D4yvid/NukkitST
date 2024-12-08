package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemCookie extends ItemEdible {

    public ItemCookie() {
        this(0, 1);
    }

    public ItemCookie(Integer meta) {
        this(meta, 1);
    }

    public ItemCookie(Integer meta, int count) {
        super(COOKIE, meta, count, "Cookie");
    }
}
