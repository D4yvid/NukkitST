package org.crimsonmc.item.enchantment.bow;

import org.crimsonmc.item.enchantment.Enchantment;
import org.crimsonmc.item.enchantment.EnchantmentType;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class EnchantmentBow extends Enchantment {

    protected EnchantmentBow(int id, String name, int weight) {
        super(id, name, weight, EnchantmentType.BOW);
    }
}
