package org.crimsonmc.event.entity;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.potion.Effect;

import java.util.Map;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntityDamageByEntityEvent extends EntityDamageEvent {

    private final Entity damager;

    private float knockBack;

    public EntityDamageByEntityEvent(Entity damager, Entity entity, int cause, float damage) {
        this(damager, entity, cause, damage, 0.4f);
    }

    public EntityDamageByEntityEvent(Entity damager, Entity entity, int cause,
                                     Map<Integer, Float> damage) {
        this(damager, entity, cause, damage, 0.4f);
    }

    public EntityDamageByEntityEvent(Entity damager, Entity entity, int cause, float damage,
                                     float knockBack) {
        super(entity, cause, damage);
        this.damager = damager;
        this.knockBack = knockBack;
        this.addAttackerModifiers(damager);
    }

    public EntityDamageByEntityEvent(Entity damager, Entity entity, int cause,
                                     Map<Integer, Float> damage, float knockBack) {
        super(entity, cause, damage);
        this.damager = damager;
        this.knockBack = knockBack;
        this.addAttackerModifiers(damager);
    }

    protected void addAttackerModifiers(Entity damager) {
        if (damager.hasEffect(Effect.STRENGTH)) {
            this.setDamage((float) (this.getDamage(MODIFIER_BASE) * 0.3 *
                            (damager.getEffect(Effect.STRENGTH).getAmplifier() + 1)),
                    MODIFIER_STRENGTH);
        }

        if (damager.hasEffect(Effect.WEAKNESS)) {
            this.setDamage(-(float) (this.getDamage(MODIFIER_BASE) * 0.2 *
                            (damager.getEffect(Effect.WEAKNESS).getAmplifier() + 1)),
                    MODIFIER_WEAKNESS);
        }
    }

    public Entity getDamager() {
        return damager;
    }

    public float getKnockBack() {
        return knockBack;
    }

    public void setKnockBack(float knockBack) {
        this.knockBack = knockBack;
    }
}
