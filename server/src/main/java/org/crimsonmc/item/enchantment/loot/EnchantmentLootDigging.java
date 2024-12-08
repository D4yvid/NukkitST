package org.crimsonmc.item.enchantment.loot;

import org.crimsonmc.item.enchantment.Enchantment;
import org.crimsonmc.item.enchantment.EnchantmentType;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EnchantmentLootDigging extends EnchantmentLoot {

    public EnchantmentLootDigging() {
        super(Enchantment.ID_FORTUNE_DIGGING, "lootBonusDigger", 2, EnchantmentType.DIGGER);
    }
}
