package org.crimsonmc.entity;

import org.crimsonmc.entity.data.PositionEntityData;
import org.crimsonmc.entity.data.Skin;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.network.protocol.AddPlayerPacket;
import org.crimsonmc.network.protocol.RemoveEntityPacket;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.util.Utils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntityHuman extends EntityHumanType {

    public static final int DATA_PLAYER_FLAG_SLEEP = 1;

    public static final int DATA_PLAYER_FLAG_DEAD = 2;

    public static final int DATA_PLAYER_FLAGS = 16;

    public static final int DATA_PLAYER_BED_POSITION = 17;

    protected UUID uuid;

    protected byte[] rawUUID;

    protected Skin skin;

    public EntityHuman(FullChunk chunk, CompoundTag nbt) {
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
    public float getEyeHeight() {
        return 1.62f;
    }

    @Override
    public int getNetworkId() {
        return -1;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public UUID getUniqueId() {
        return getUuid();
    }

    public byte[] getRawUniqueId() {
        return getRawUUID();
    }

    @Override
    protected void initEntity() {
        this.setDataFlag(DATA_PLAYER_FLAGS, DATA_PLAYER_FLAG_SLEEP, false);

        this.setDataProperty(new PositionEntityData(DATA_PLAYER_BED_POSITION, 0, 0, 0), false);

        if (!(this instanceof ServerPlayer)) {
            if (this.namedTag.contains("NameTag")) {
                this.setNameTag(this.namedTag.getString("NameTag"));
            }

            if (this.namedTag.contains("Skin") && this.namedTag.get("Skin") instanceof CompoundTag) {
                if (!this.namedTag.getCompound("Skin").contains("Transparent")) {
                    this.namedTag.getCompound("Skin").putBoolean("Transparent", false);
                }
                this.setSkin(new Skin(this.namedTag.getCompound("Skin").getByteArray("Data"),
                        this.namedTag.getCompound("Skin").getString("ModelId")));
            }

            this.setUuid(Utils.dataToUUID(String.valueOf(this.getId()).getBytes(StandardCharsets.UTF_8),
                    this.getSkin().getData(),
                    this.getNameTag().getBytes(StandardCharsets.UTF_8)));
        }

        super.initEntity();

        if (this instanceof ServerPlayer) {
            ((ServerPlayer) this).addWindow(this.inventory, 0);
        }
    }

    @Override
    public String getName() {
        return this.getNameTag();
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        if (this.getSkin().getData().length > 0) {
            this.namedTag.putCompound("Skin", new CompoundTag()
                    .putByteArray("Data", this.getSkin().getData())
                    .putString("ModelId", this.getSkin().getModel()));
        }
    }

    @Override
    public void spawnTo(ServerPlayer player) {
        if (this != player && !this.hasSpawned.containsKey(player.getLoaderId())) {
            this.hasSpawned.put(player.getLoaderId(), player);

            if (this.skin.getData().length < 64 * 32 * 4) {
                throw new IllegalStateException(this.getClass().getSimpleName() +
                        " must have a valid skin set");
            }

            if (!(this instanceof ServerPlayer)) {
                this.server.updatePlayerListData(this.getUniqueId(), this.getId(), this.getName(),
                        this.skin, new ServerPlayer[]{player});
            }

            AddPlayerPacket pk = new AddPlayerPacket();
            pk.uuid = this.getUniqueId();
            pk.username = this.getName();
            pk.eid = this.getId();
            pk.x = (float) this.x;
            pk.y = (float) this.y;
            pk.z = (float) this.z;
            pk.speedX = (float) this.motionX;
            pk.speedY = (float) this.motionY;
            pk.speedZ = (float) this.motionZ;
            pk.yaw = (float) this.yaw;
            pk.pitch = (float) this.pitch;
            pk.item = this.getInventory().getItemInHand();
            pk.metadata = this.dataProperties;
            player.dataPacket(pk);

            this.inventory.sendArmorContents(player);

            if (!(this instanceof ServerPlayer)) {
                this.server.removePlayerListData(this.getUniqueId(), new ServerPlayer[]{player});
            }
        }
    }

    @Override
    public void despawnFrom(ServerPlayer player) {
        if (this.hasSpawned.containsKey(player.getLoaderId())) {

            RemoveEntityPacket pk = new RemoveEntityPacket();
            pk.eid = this.getId();
            player.dataPacket(pk);
            this.hasSpawned.remove(player.getLoaderId());
        }
    }

    @Override
    public void close() {
        if (!this.closed) {
            if (!(this instanceof ServerPlayer) || ((ServerPlayer) this).loggedIn) {
                for (ServerPlayer viewer : this.inventory.getViewers()) {
                    viewer.removeWindow(this.inventory);
                }
            }

            super.close();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        if (this.uuid != null)
            throw new RuntimeException("The field was already set");

        this.uuid = uuid;
    }

    public byte[] getRawUUID() {
        return rawUUID;
    }

    public void setRawUUID(byte[] rawUUID) {
        if (this.rawUUID != null)
            throw new RuntimeException("The field was already set");

        this.rawUUID = rawUUID;
    }
}
