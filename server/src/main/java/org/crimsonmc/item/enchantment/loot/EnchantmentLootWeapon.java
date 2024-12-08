package org.crimsonmc.item.enchantment.loot;

import org.crimsonmc.item.enchantment.Enchantment;
import org.crimsonmc.item.enchantment.EnchantmentType;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EnchantmentLootWeapon extends EnchantmentLoot {

    public EnchantmentLootWeapon() {
        super(Enchantment.ID_LOOTING, "lootBonus", 2, EnchantmentType.SWORD);
    }
}
