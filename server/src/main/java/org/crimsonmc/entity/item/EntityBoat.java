package org.crimsonmc.entity.item;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.event.entity.EntityDamageByEntityEvent;
import org.crimsonmc.event.entity.EntityDamageEvent;
import org.crimsonmc.item.ItemBoat;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.level.particle.SmokeParticle;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.network.protocol.AddEntityPacket;
import org.crimsonmc.network.protocol.EntityEventPacket;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.server.Server;

/**
 * Created by yescallop on 2016/2/13.
 */
public class EntityBoat extends EntityVehicle {

    public static final int NETWORK_ID = 90;

    public static final int DATA_WOOD_ID = 20;

    public EntityBoat(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.dataProperties.putByte(DATA_WOOD_ID, this.namedTag.getByte("woodID"));

        this.setHealth(4);
        this.setMaxHealth(4);
    }

    @Override
    public float getHeight() {
        return 0.7f;
    }

    @Override
    public float getWidth() {
        return 1.6f;
    }

    @Override
    protected float getDrag() {
        return 0.1f;
    }

    @Override
    protected float getGravity() {
        return 0.1f;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void spawnTo(ServerPlayer player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.eid = this.getId();
        pk.type = EntityBoat.NETWORK_ID;
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = (float) this.yaw;
        pk.pitch = (float) this.pitch;
        pk.metadata = this.dataProperties;
        player.dataPacket(pk);

        super.spawnTo(player);
    }

    @Override
    public void attack(EntityDamageEvent source) {
        super.attack(source);
        if (source.isCancelled())
            return;
        if (source instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) source).getDamager();
            if (damager instanceof ServerPlayer) {
                if (((ServerPlayer) damager).isCreative()) {
                    this.kill();
                }
                if (this.getHealth() <= 0) {
                    if (((ServerPlayer) damager).isSurvival()) {
                        this.level.dropItem(this, new ItemBoat());
                    }
                    this.close();
                }
            }
        }

        EntityEventPacket pk = new EntityEventPacket();
        pk.eid = this.getId();
        pk.event = this.getHealth() <= 0 ? EntityEventPacket.DEATH_ANIMATION
                : EntityEventPacket.HURT_ANIMATION;
        Server.broadcastPacket(this.hasSpawned.values(), pk);
    }

    @Override
    public void close() {
        super.close();

        if (this.linkedEntity instanceof ServerPlayer) {
            this.linkedEntity.riding = null;
        }

        SmokeParticle particle = new SmokeParticle(this);
        this.level.addParticle(particle);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        int tickDiff = currentTick - this.lastUpdate;

        if (tickDiff <= 0 && !this.justCreated) {
            return true;
        }

        this.lastUpdate = currentTick;

        boolean hasUpdate = this.entityBaseTick(tickDiff);

        if (this.isAlive()) {

            this.motionY =
                    (this.level.getBlock(new Vector3(this.x, this.y, this.z)).getBoundingBox() != null ||
                            this.isInsideOfWater())
                            ? getGravity()
                            : -0.08;

            if (this.checkObstruction(this.x, this.y, this.z)) {
                hasUpdate = true;
            }

            this.move(this.motionX, this.motionY, this.motionZ);

            double friction = 1 - this.getDrag();

            if (this.onGround && (Math.abs(this.motionX) > 0.00001 || Math.abs(this.motionZ) > 0.00001)) {
                friction *= this.getLevel()
                        .getBlock(this.getTemporalVector().setComponents(
                                (int) Math.floor(this.x), (int) Math.floor(this.y - 1),
                                (int) Math.floor(this.z) - 1))
                        .getFrictionFactor();
            }

            this.motionX *= friction;
            this.motionY *= 1 - this.getDrag();
            this.motionZ *= friction;

            if (this.onGround) {
                this.motionY *= -0.5;
            }

            this.updateMovement();
        }

        return hasUpdate || !this.onGround || Math.abs(this.motionX) > 0.00001 ||
                Math.abs(this.motionY) > 0.00001 || Math.abs(this.motionZ) > 0.00001;
    }
}