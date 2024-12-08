package org.crimsonmc.entity.passive;

import org.crimsonmc.entity.EntityAgeable;
import org.crimsonmc.entity.EntityCreature;
import org.crimsonmc.entity.data.ByteEntityData;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.network.protocol.AddEntityPacket;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class EntityAnimal extends EntityCreature implements EntityAgeable {

    public EntityAnimal(FullChunk chunk, CompoundTag nbt) {
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

    public void spawnTo(ServerPlayer player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = this.getNetworkId();
        pk.eid = this.getId();
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.speedX = (float) this.motionX;
        pk.speedY = (float) this.motionY;
        pk.speedZ = (float) this.motionZ;
        pk.metadata = this.dataProperties;
        player.dataPacket(pk);

        super.spawnTo(player);
    }
}
