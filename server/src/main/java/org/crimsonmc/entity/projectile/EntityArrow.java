package org.crimsonmc.entity.projectile;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.level.particle.CriticalParticle;
import org.crimsonmc.math.MathUtilities;
import org.crimsonmc.math.RandomUtilities;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.network.protocol.AddEntityPacket;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class EntityArrow extends EntityProjectile {

    public static final int NETWORK_ID = 80;

    public static final int DATA_SOURCE_ID = 17;

    protected float gravity = 0.05f;

    protected float drag = 0.01f;

    protected double damage = 2;

    protected boolean isCritical;

    public EntityArrow(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityArrow(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        this(chunk, nbt, shootingEntity, false);
    }

    public EntityArrow(FullChunk chunk, CompoundTag nbt, Entity shootingEntity, boolean critical) {
        super(chunk, nbt, shootingEntity);
        this.isCritical = critical;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.5f;
    }

    @Override
    public float getLength() {
        return 0.5f;
    }

    @Override
    public float getHeight() {
        return 0.5f;
    }

    @Override
    public float getGravity() {
        return 0.05f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    protected double getDamage() {
        return namedTag.contains("damage") ? namedTag.getDouble("damage") : 2;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        boolean hasUpdate = super.onUpdate(currentTick);

        if (!this.hadCollision && this.isCritical) {
            RandomUtilities random = new RandomUtilities();
            this.level.addParticle(new CriticalParticle(this.add(
                    this.getWidth() / 2 + ((double) MathUtilities.randomRange(random, -100, 100)) / 500,
                    this.getHeight() / 2 + ((double) MathUtilities.randomRange(random, -100, 100)) / 500,
                    this.getWidth() / 2 + ((double) MathUtilities.randomRange(random, -100, 100)) / 500)));
        } else if (this.onGround) {
            this.isCritical = false;
        }

        if (this.age > 1200) {
            this.kill();
            hasUpdate = true;
        }

        return hasUpdate;
    }

    @Override
    public void spawnTo(ServerPlayer player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = EntityArrow.NETWORK_ID;
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
