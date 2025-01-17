package org.crimsonmc.entity.passive;

import org.crimsonmc.entity.EntityAgeable;
import org.crimsonmc.entity.EntityCreature;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.network.protocol.AddEntityPacket;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created by Pub4Game on 21.06.2016.
 */
public class EntityVillager extends EntityCreature implements EntityNPC, EntityAgeable {

    public static final int PROFESSION_FARMER = 0;

    public static final int PROFESSION_LIBRARIAN = 1;

    public static final int PROFESSION_PRIEST = 2;

    public static final int PROFESSION_BLACKSMITH = 3;

    public static final int PROFESSION_BUTCHER = 4;

    public static final int PROFESSION_GENERIC = 5;

    public static final int NETWORK_ID = 15;

    public EntityVillager(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getLength() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    @Override
    public String getName() {
        return "Villager";
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        if (!this.namedTag.contains("Profession")) {
            this.setProfession(PROFESSION_GENERIC);
        }
    }

    public int getProfession() {
        return this.namedTag.getInt("Profession");
    }

    public void setProfession(int profession) {
        this.namedTag.putInt("Profession", profession);
    }

    @Override
    public boolean isBaby() {
        return this.getDataFlag(DATA_AGEABLE_FLAGS, DATA_FLAG_BABY);
    }

    @Override
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
