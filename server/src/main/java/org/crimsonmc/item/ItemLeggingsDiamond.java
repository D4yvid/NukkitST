package org.crimsonmc.item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemLeggingsDiamond extends ItemArmor {

    public ItemLeggingsDiamond() {
        this(0, 1);
    }

    public ItemLeggingsDiamond(Integer meta) {
        this(meta, 1);
    }

    public ItemLeggingsDiamond(Integer meta, int count) {
        super(DIAMOND_LEGGINGS, meta, count, "Diamond Leggings");
    }

    @Override
    public boolean isLeggings() {
        return true;
    }

    @Override
    public int getTier() {
        return ItemArmor.TIER_DIAMOND;
    }

    @Override
    public int getArmorPoints() {
        return 6;
    }

    @Override
    public int getMaxDurability() {
        return 496;
    }
}
