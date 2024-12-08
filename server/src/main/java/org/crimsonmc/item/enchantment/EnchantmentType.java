package org.crimsonmc.item.enchantment;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemArmor;
import org.crimsonmc.item.ItemBow;
import org.crimsonmc.item.ItemFishingRod;

/**
 * author: MagicDroidX crimsonmc Project
 */
public enum EnchantmentType {
    ALL,
    ARMOR,
    ARMOR_HEAD,
    ARMOR_TORSO,
    ARMOR_LEGS,
    ARMOR_FEET,
    SWORD,
    DIGGER,
    FISHING_ROD,
    BREAKABLE,
    BOW;

    public boolean canEnchantItem(Item item) {
        if (this == ALL) {
            return true;

        } else if (this == BREAKABLE && item.getMaxDurability() >= 0) {
            return true;

        } else if (item instanceof ItemArmor) {
            if (this == ARMOR) {
                return true;
            }

            switch (this) {
                case ARMOR_HEAD:
                    return item.isHelmet();
                case ARMOR_TORSO:
                    return item.isChestplate();
                case ARMOR_LEGS:
                    return item.isLeggings();
                case ARMOR_FEET:
                    return item.isBoots();
                default:
                    return false;
            }

        } else {
            switch (this) {
                case SWORD:
                    return item.isSword();
                case DIGGER:
                    return item.isPickaxe() || item.isShovel() || item.isAxe();
                case BOW:
                    return item instanceof ItemBow;
                case FISHING_ROD:
                    return item instanceof ItemFishingRod;
                default:
                    return false;
            }
        }
    }
}
