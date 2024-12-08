package org.crimsonmc.entity.item;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.entity.EntityRideable;
import org.crimsonmc.entity.data.FloatEntityData;
import org.crimsonmc.entity.data.IntEntityData;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.nbt.tag.CompoundTag;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class EntityVehicle extends Entity implements EntityRideable {

    public static final int DATA_HURT_TIME = 17;

    public static final int DATA_HURT_DIRECTION = 18;

    public static final int DATA_DAMAGE_TAKEN = 19;

    public Entity linkedEntity = null;

    public EntityVehicle(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public int getHurtTime() {
        return this.getDataPropertyInt(EntityVehicle.DATA_HURT_TIME);
    }

    public void setHurtTime(int time) {
        this.setDataProperty(new IntEntityData(EntityVehicle.DATA_HURT_TIME, time));
    }

    public int getHurtDirection() {
        return this.getDataPropertyInt(EntityVehicle.DATA_HURT_DIRECTION);
    }

    public void setHurtDirection(int direction) {
        this.setDataProperty(new IntEntityData(EntityVehicle.DATA_HURT_DIRECTION, direction));
    }

    public float getDamageTaken() {
        return this.getDataPropertyFloat(EntityVehicle.DATA_DAMAGE_TAKEN);
    }

    public void setDamageTaken(float damage) {
        this.setDataProperty(new FloatEntityData(EntityVehicle.DATA_DAMAGE_TAKEN, damage));
    }

    public Entity getLinkedEntity() {
        return linkedEntity;
    }

    public void setLinkedEntity(Entity entity) {
        linkedEntity = entity;
    }
}
