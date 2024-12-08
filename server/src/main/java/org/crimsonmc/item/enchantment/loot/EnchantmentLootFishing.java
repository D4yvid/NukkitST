package org.crimsonmc.item.enchantment.loot;

import org.crimsonmc.item.enchantment.Enchantment;
import org.crimsonmc.item.enchantment.EnchantmentType;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EnchantmentLootFishing extends EnchantmentLoot {

    public EnchantmentLootFishing() {
        super(Enchantment.ID_FORTUNE_FISHING, "lootBonusFishing", 2, EnchantmentType.FISHING_ROD);
    }
}
