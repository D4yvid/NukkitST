package org.crimsonmc.item.enchantment.loot;

import org.crimsonmc.item.enchantment.Enchantment;
import org.crimsonmc.item.enchantment.EnchantmentType;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class EnchantmentLoot extends Enchantment {

    protected EnchantmentLoot(int id, String name, int weight, EnchantmentType type) {
        super(id, name, weight, type);
    }

    @Override
    public int getMinEnchantAbility(int level) {
        return 15 + (level - 1) * 9;
    }

    @Override
    public int getMaxEnchantAbility(int level) {
        return this.getMinEnchantAbility(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isCompatibleWith(Enchantment enchantment) {
        return super.isCompatibleWith(enchantment) && enchantment.id != Enchantment.ID_SILK_TOUCH;
    }
}
