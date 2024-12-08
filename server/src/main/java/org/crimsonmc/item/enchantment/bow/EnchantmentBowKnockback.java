package org.crimsonmc.item.enchantment.bow;

import org.crimsonmc.item.enchantment.Enchantment;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EnchantmentBowKnockback extends EnchantmentBow {

    public EnchantmentBowKnockback() {
        super(Enchantment.ID_BOW_KNOCKBACK, "arrowKnockback", 2);
    }

    @Override
    public int getMinEnchantAbility(int level) {
        return 12 + (level - 1) * 20;
    }

    @Override
    public int getMaxEnchantAbility(int level) {
        return this.getMinEnchantAbility(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
