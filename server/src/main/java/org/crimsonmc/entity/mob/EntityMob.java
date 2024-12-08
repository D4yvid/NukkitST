package org.crimsonmc.entity.mob;

import org.crimsonmc.entity.EntityCreature;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.nbt.tag.CompoundTag;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class EntityMob extends EntityCreature {

    public EntityMob(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
}
