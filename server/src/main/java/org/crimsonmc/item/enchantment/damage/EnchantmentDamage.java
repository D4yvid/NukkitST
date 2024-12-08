package org.crimsonmc.item.enchantment.damage;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.entity.EntityArthropod;
import org.crimsonmc.entity.EntityLiving;
import org.crimsonmc.entity.EntitySmite;
import org.crimsonmc.item.Item;
import org.crimsonmc.item.enchantment.Enchantment;
import org.crimsonmc.item.enchantment.EnchantmentType;
import org.crimsonmc.potion.Effect;

import java.util.concurrent.ThreadLocalRandom;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class EnchantmentDamage extends Enchantment {

    protected final TYPE damageType;

    protected EnchantmentDamage(int id, String name, int weight, TYPE type) {
        super(id, name, weight, EnchantmentType.SWORD);
        this.damageType = type;
    }

    @Override
    public double getDamageBonus(Entity entity) {
        int level = this.level;

        switch (this.damageType) {
            case ARTHROPODS:
                if (entity instanceof EntityArthropod) {
                    return (double) level * 2.5;
                }
            case SMITE:
                if (entity instanceof EntitySmite) {
                    return (double) level * 2.5;
                }
            case ALL:
                return (double) level * 1.25;
        }

        return 0;
    }

    @Override
    public boolean isCompatibleWith(Enchantment enchantment) {
        return !(enchantment instanceof EnchantmentDamage);
    }

    @Override
    public boolean canEnchant(Item item) {
        return item.isAxe() || super.canEnchant(item);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public void doPostAttack(Entity attacker, Entity entity) {
        if (attacker instanceof EntityLiving) {
            if (this.damageType == TYPE.ARTHROPODS && entity instanceof EntityArthropod) {
                int duration = 20 + ThreadLocalRandom.current().nextInt(10 * this.level);
                entity.addEffect(Effect.getEffect(Effect.SLOWNESS).setDuration(duration).setAmplifier(3));
            }
        }
    }

    @Override
    public String getName() {
        return "%enchantment.damage." + this.name;
    }

    @Override
    public boolean isMajor() {
        return true;
    }

    public enum TYPE {ALL, SMITE, ARTHROPODS}
}
