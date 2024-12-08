package org.crimsonmc.entity.passive;

import org.crimsonmc.entity.EntityAgeable;
import org.crimsonmc.entity.EntityCreature;
import org.crimsonmc.entity.data.ByteEntityData;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.nbt.tag.CompoundTag;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class EntityWaterAnimal extends EntityCreature implements EntityAgeable {

    public EntityWaterAnimal(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        if (this.getDataProperty(DATA_AGEABLE_FLAGS) == null) {
            this.setDataProperty(new ByteEntityData(DATA_AGEABLE_FLAGS, 0));
        }
    }

    @Override
    public boolean isBaby() {
        return this.getDataFlag(DATA_AGEABLE_FLAGS, DATA_FLAG_BABY);
    }
}
