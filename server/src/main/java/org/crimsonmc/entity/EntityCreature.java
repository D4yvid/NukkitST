package org.crimsonmc.entity;

import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.nbt.tag.CompoundTag;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class EntityCreature extends EntityLiving {

    public EntityCreature(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
}
