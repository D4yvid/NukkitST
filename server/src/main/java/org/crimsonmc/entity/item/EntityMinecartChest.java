package org.crimsonmc.entity.item;

import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.nbt.tag.CompoundTag;

/**
 * Created by Snake1999 on 2016/1/30. Package cn.crimsonmc.entity.item in project crimsonmc.
 */
public class EntityMinecartChest extends EntityMinecartEmpty {

    // TODO: 2016/1/30 NETWORK_ID

    public EntityMinecartChest(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    // TODO: 2016/1/30 inventory
}
