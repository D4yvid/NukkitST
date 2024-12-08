package org.crimsonmc.item.enchantment.bow;

import org.crimsonmc.item.enchantment.Enchantment;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EnchantmentBowFlame extends EnchantmentBow {

    public EnchantmentBowFlame() {
        super(Enchantment.ID_BOW_FLAME, "arrowFire", 2);
    }

    @Override
    public int getMinEnchantAbility(int level) {
        return 20;
    }

    @Override
    public int getMaxEnchantAbility(int level) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
