package org.crimsonmc.item.enchantment.damage;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EnchantmentDamageSmite extends EnchantmentDamage {

    public EnchantmentDamageSmite() {
        super(ID_DAMAGE_SMITE, "undead", 5, TYPE.SMITE);
    }

    @Override
    public int getMinEnchantAbility(int level) {
        return 5 + (level - 1) * 8;
    }

    @Override
    public int getMaxEnchantAbility(int level) {
        return this.getMinEnchantAbility(level) + 20;
    }
}
