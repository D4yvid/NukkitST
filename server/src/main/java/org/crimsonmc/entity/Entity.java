package org.crimsonmc.entity;

import org.crimsonmc.block.Block;
import org.crimsonmc.block.BlockDirt;
import org.crimsonmc.block.BlockFire;
import org.crimsonmc.block.BlockWater;
import org.crimsonmc.entity.data.*;
import org.crimsonmc.event.entity.*;
import org.crimsonmc.event.player.PlayerTeleportEvent;
import org.crimsonmc.exception.ChunkException;
import org.crimsonmc.item.Item;
import org.crimsonmc.level.Level;
import org.crimsonmc.level.Location;
import org.crimsonmc.level.Position;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.math.AxisAlignedBB;
import org.crimsonmc.math.MathUtilities;
import org.crimsonmc.math.Vector2;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.metadata.MetadataValue;
import org.crimsonmc.metadata.Metadatable;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.nbt.tag.DoubleTag;
import org.crimsonmc.nbt.tag.FloatTag;
import org.crimsonmc.nbt.tag.ListTag;
import org.crimsonmc.network.protocol.MobEffectPacket;
import org.crimsonmc.network.protocol.RemoveEntityPacket;
import org.crimsonmc.network.protocol.SetEntityDataPacket;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.plugin.Plugin;
import org.crimsonmc.potion.Effect;
import org.crimsonmc.server.Server;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MagicDroidX
 */
public abstract class Entity extends Location implements Metadatable {

    public static final int NETWORK_ID = -1;

    public static final int DATA_TYPE_BYTE = 0;

    public static final int DATA_TYPE_SHORT = 1;

    public static final int DATA_TYPE_INT = 2;

    public static final int DATA_TYPE_FLOAT = 3;

    public static final int DATA_TYPE_STRING = 4;

    public static final int DATA_TYPE_SLOT = 5;

    public static final int DATA_TYPE_POS = 6;

    public static final int DATA_TYPE_LONG = 7;

    public static final int DATA_FLAGS = 0;
    // public static final int DATA_TYPE_ROTATION = 7;
    // public static final int DATA_TYPE_LONG = 8;

    public static final int DATA_AIR = 1;

    public static final int DATA_NAMETAG = 2;

    public static final int DATA_SHOW_NAMETAG = 3;

    public static final int DATA_SILENT = 4;

    public static final int DATA_POTION_COLOR = 7;

    public static final int DATA_POTION_AMBIENT = 8;

    public static final int DATA_NO_AI = 15;

    public static final int DATA_FLAG_ONFIRE = 0;

    public static final int DATA_FLAG_SNEAKING = 1;

    public static final int DATA_FLAG_RIDING = 2;

    public static final int DATA_FLAG_SPRINTING = 3;

    public static final int DATA_FLAG_ACTION = 4;

    public static final int DATA_FLAG_INVISIBLE = 5;

    public static final int DATA_LEAD_HOLDER = 23;

    public static final int DATA_LEAD = 24;

    private static final Map<String, Class<? extends Entity>> knownEntities = new HashMap<>();

    private static final Map<String, String> shortNames = new HashMap<>();

    public static long entityCount = 1;

    protected final Map<Integer, Effect> effects = new ConcurrentHashMap<>();

    protected final EntityMetadata dataProperties = new EntityMetadata()
            .putByte(DATA_FLAGS, 0)
            .putShort(DATA_AIR, 300)
            .putString(DATA_NAMETAG, "")
            .putBoolean(DATA_SHOW_NAMETAG, true)
            .putBoolean(DATA_SILENT, false)
            .putBoolean(DATA_NO_AI, false)
            .putLong(DATA_LEAD_HOLDER, -1)
            .putByte(DATA_LEAD, 0);

    public Entity rider = null;

    public Entity riding = null;

    public FullChunk chunk;

    public double lastX;

    public double lastY;

    public double lastZ;

    public boolean firstMove = true;

    public double motionX;

    public double motionY;

    public double motionZ;
    public double lastMotionX;
    public double lastMotionY;
    public double lastMotionZ;
    public double lastYaw;
    public double lastPitch;
    public AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean inBlock = false;
    public boolean positionChanged;
    public boolean motionChanged;
    public int deadTicks = 0;
    public boolean keepMovement = false;
    public float fallDistance = 0;
    public int ticksLived = 0;
    public int lastUpdate;
    public int maxFireTicks;
    public int fireTicks = 0;
    public int inPortalTicks = 0;
    public CompoundTag namedTag;
    public boolean isCollided = false;
    public boolean isCollidedHorizontally = false;
    public boolean isCollidedVertically = false;
    public int noDamageTicks;
    public boolean justCreated;
    public boolean fireProof;
    public boolean invulnerable;
    public double highestPosition;
    public boolean closed = false;
    protected Map<Integer, ServerPlayer> hasSpawned = new HashMap<>();
    protected long id;
    protected int dataFlags = 0;
    protected EntityDamageEvent lastDamageCause = null;
    protected int age = 0;
    protected int health = 20;
    protected float ySize = 0;
    protected boolean isStatic = false;
    protected Server server;
    protected boolean isPlayer = false;
    private Vector3 temporalVector;
    private List<Block> blocksAround = new ArrayList<>();

    private int maxHealth = 20;

    public Entity(FullChunk chunk, CompoundTag nbt) {
        if (this instanceof ServerPlayer) {
            return;
        }

        this.init(chunk, nbt);
    }

    public static Entity createEntity(String name, FullChunk chunk, CompoundTag nbt, Object... args) {
        Entity entity = null;

        if (knownEntities.containsKey(name)) {
            Class<? extends Entity> clazz = knownEntities.get(name);

            if (clazz == null) {
                return null;
            }

            for (Constructor constructor : clazz.getConstructors()) {
                if (entity != null) {
                    break;
                }

                if (constructor.getParameterCount() != (args == null ? 2 : args.length + 2)) {
                    continue;
                }

                try {
                    if (args == null || args.length == 0) {
                        entity = (Entity) constructor.newInstance(chunk, nbt);
                    } else {
                        Object[] objects = new Object[args.length + 2];

                        objects[0] = chunk;
                        objects[1] = nbt;
                        System.arraycopy(args, 0, objects, 2, args.length);
                        entity = (Entity) constructor.newInstance(objects);
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
        }

        return entity;
    }

    public static Entity createEntity(int type, FullChunk chunk, CompoundTag nbt, Object... args) {
        return createEntity(String.valueOf(type), chunk, nbt, args);
    }

    public static boolean registerEntity(String name, Class<? extends Entity> clazz) {
        return registerEntity(name, clazz, false);
    }

    public static boolean registerEntity(String name, Class<? extends Entity> clazz, boolean force) {
        if (clazz == null) {
            return false;
        }
        try {
            int networkId = clazz.getField("NETWORK_ID").getInt(null);
            knownEntities.put(String.valueOf(networkId), clazz);
        } catch (Exception e) {
            if (!force) {
                return false;
            }
        }

        knownEntities.put(name, clazz);
        shortNames.put(clazz.getSimpleName(), name);
        return true;
    }

    public abstract int getNetworkId();

    public float getHeight() {
        return 0;
    }

    public float getEyeHeight() {
        return this.getHeight() / 2 + 0.1f;
    }

    public float getWidth() {
        return 0;
    }

    public float getLength() {
        return 0;
    }

    protected double getStepHeight() {
        return 0;
    }

    public boolean canCollide() {
        return true;
    }

    protected float getGravity() {
        return 0;
    }

    protected float getDrag() {
        return 0;
    }

    protected void initEntity() {
        if (this.namedTag.contains("ActiveEffects")) {
            ListTag<CompoundTag> effects = this.namedTag.getList("ActiveEffects", CompoundTag.class);
            for (CompoundTag e : effects.getAll()) {
                Effect effect = Effect.getEffect(e.getByte("Id"));
                if (effect == null) {
                    continue;
                }

                effect.setAmplifier(e.getByte("Amplifier"))
                        .setDuration(e.getInt("Duration"))
                        .setVisible(e.getBoolean("showParticles"));

                this.addEffect(effect);
            }
        }

        if (this.namedTag.contains("CustomName")) {
            this.setNameTag(this.namedTag.getString("CustomName"));
            if (this.namedTag.contains("CustomNameVisible")) {
                this.setNameTagVisible(this.namedTag.getBoolean("CustomNameVisible"));
            }
        }

        this.scheduleUpdate();
    }

    protected final void init(FullChunk chunk, CompoundTag nbt) {
        if ((chunk == null || chunk.getProvider() == null)) {
            throw new ChunkException("Invalid garbage Chunk given to Entity");
        }

        this.isPlayer = this instanceof ServerPlayer;
        this.setTemporalVector(new Vector3());

        this.id = Entity.entityCount++;
        this.justCreated = true;
        this.namedTag = nbt;

        this.chunk = chunk;
        this.setLevel(chunk.getProvider().getLevel());
        this.server = chunk.getProvider().getLevel().getServer();

        this.boundingBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

        ListTag<DoubleTag> posList = this.namedTag.getList("Pos", DoubleTag.class);
        ListTag<FloatTag> rotationList = this.namedTag.getList("Rotation", FloatTag.class);
        ListTag<DoubleTag> motionList = this.namedTag.getList("Motion", DoubleTag.class);
        this.setPositionAndRotation(this.getTemporalVector().setComponents(
                        posList.get(0).data, posList.get(1).data, posList.get(2).data),
                rotationList.get(0).data, rotationList.get(1).data);

        this.setMotion(this.getTemporalVector().setComponents(
                motionList.get(0).data, motionList.get(1).data, motionList.get(2).data));

        if (!this.namedTag.contains("FallDistance")) {
            this.namedTag.putFloat("FallDistance", 0);
        }
        this.fallDistance = this.namedTag.getFloat("FallDistance");
        this.highestPosition = this.y + this.namedTag.getFloat("FallDistance");

        if (!this.namedTag.contains("Fire") || this.namedTag.getShort("Fire") > 32767) {
            this.namedTag.putShort("Fire", 0);
        }
        this.fireTicks = this.namedTag.getShort("Fire");

        if (!this.namedTag.contains("Air")) {
            this.namedTag.putShort("Air", 300);
        }
        this.setDataProperty(new ShortEntityData(DATA_AIR, this.namedTag.getShort("Air")), false);

        if (!this.namedTag.contains("OnGround")) {
            this.namedTag.putBoolean("OnGround", false);
        }
        this.onGround = this.namedTag.getBoolean("OnGround");

        if (!this.namedTag.contains("Invulnerable")) {
            this.namedTag.putBoolean("Invulnerable", false);
        }
        this.invulnerable = this.namedTag.getBoolean("Invulnerable");

        this.chunk.addEntity(this);
        this.level.addEntity(this);

        this.initEntity();

        this.lastUpdate = this.server.getTick();
        this.server.getPluginManager().callEvent(new EntitySpawnEvent(this));

        this.scheduleUpdate();
    }

    public boolean hasCustomName() {
        return !this.getNameTag().isEmpty();
    }

    public String getNameTag() {
        return this.getDataPropertyString(DATA_NAMETAG);
    }

    public void setNameTag(String name) {
        this.setDataProperty(new StringEntityData(DATA_NAMETAG, name));
    }

    public boolean isNameTagVisible() {
        return this.getDataPropertyBoolean(DATA_SHOW_NAMETAG);
    }

    public void setNameTagVisible(boolean visible) {
        this.setDataProperty(new ByteEntityData(DATA_SHOW_NAMETAG, visible ? 1 : 0));
    }

    public void setNameTagVisible() {
        this.setNameTagVisible(true);
    }

    public boolean isSneaking() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_SNEAKING);
    }

    public void setSneaking(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SNEAKING, value);
    }

    public void setSneaking() {
        this.setSneaking(true);
    }

    public boolean isSprinting() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_SPRINTING);
    }

    public void setSprinting(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SPRINTING, value);
    }

    public void setSprinting() {
        this.setSprinting(true);
    }

    public Map<Integer, Effect> getEffects() {
        return effects;
    }

    public void removeAllEffects() {
        for (Effect effect : this.effects.values()) {
            this.removeEffect(effect.getId());
        }
    }

    public void removeEffect(int effectId) {
        if (this.effects.containsKey(effectId)) {
            Effect effect = this.effects.get(effectId);
            this.effects.remove(effectId);
            effect.remove(this);

            this.recalculateEffectColor();
        }
    }

    public Effect getEffect(int effectId) {
        return this.effects.containsKey(effectId) ? this.effects.get(effectId) : null;
    }

    public boolean hasEffect(int effectId) {
        return this.effects.containsKey(effectId);
    }

    public void addEffect(Effect effect) {
        if (effect == null) {
            return; // here add null means add nothing
        }

        Effect oldEffect = this.effects.getOrDefault(effect.getId(), null);
        if (oldEffect != null) {
            if (Math.abs(effect.getAmplifier()) < Math.abs(oldEffect.getAmplifier()))
                return;
            if (Math.abs(effect.getAmplifier()) == Math.abs(oldEffect.getAmplifier()) &&
                    effect.getDuration() < oldEffect.getDuration())
                return;
            effect.add(this, true);
        } else {
            effect.add(this, false);
        }

        this.effects.put(effect.getId(), effect);

        this.recalculateEffectColor();

        if (effect.getId() == Effect.HEALTH_BOOST) {
            this.setHealth(this.getHealth() + 4 * (effect.getAmplifier() + 1));
        }
    }

    protected void recalculateEffectColor() {
        int[] color = new int[3];
        int count = 0;
        boolean ambient = true;
        for (Effect effect : this.effects.values()) {
            if (effect.isVisible()) {
                int[] c = effect.getColor();
                color[0] += c[0] * (effect.getAmplifier() + 1);
                color[1] += c[1] * (effect.getAmplifier() + 1);
                color[2] += c[2] * (effect.getAmplifier() + 1);
                count += effect.getAmplifier() + 1;
                if (!effect.isAmbient()) {
                    ambient = false;
                }
            }
        }

        if (count > 0) {
            int r = (color[0] / count) & 0xff;
            int g = (color[1] / count) & 0xff;
            int b = (color[2] / count) & 0xff;

            this.setDataProperty(new IntEntityData(Entity.DATA_POTION_COLOR, (r << 16) + (g << 8) + b));
            this.setDataProperty(new ByteEntityData(Entity.DATA_POTION_AMBIENT, ambient ? 1 : 0));
        } else {
            this.setDataProperty(new IntEntityData(Entity.DATA_POTION_COLOR, 0));
            this.setDataProperty(new ByteEntityData(Entity.DATA_POTION_AMBIENT, 0));
        }
    }

    public void saveNBT() {
        if (!(this instanceof ServerPlayer)) {
            this.namedTag.putString("id", this.getSaveId());
            if (!this.getNameTag().equals("")) {
                this.namedTag.putString("CustomName", this.getNameTag());
                this.namedTag.putString("CustomNameVisible", String.valueOf(this.isNameTagVisible()));
            } else {
                this.namedTag.remove("CustomName");
                this.namedTag.remove("CustomNameVisible");
            }
        }

        this.namedTag.putList(new ListTag<DoubleTag>("Pos")
                .add(new DoubleTag("0", this.x))
                .add(new DoubleTag("1", this.y))
                .add(new DoubleTag("2", this.z)));

        this.namedTag.putList(new ListTag<DoubleTag>("Motion")
                .add(new DoubleTag("0", this.motionX))
                .add(new DoubleTag("1", this.motionY))
                .add(new DoubleTag("2", this.motionZ)));

        this.namedTag.putList(new ListTag<FloatTag>("Rotation")
                .add(new FloatTag("0", (float) this.yaw))
                .add(new FloatTag("1", (float) this.pitch)));

        this.namedTag.putFloat("FallDistance", this.fallDistance);
        this.namedTag.putShort("Fire", this.fireTicks);
        this.namedTag.putShort("Air", this.getDataPropertyShort(DATA_AIR));
        this.namedTag.putBoolean("OnGround", this.onGround);
        this.namedTag.putBoolean("Invulnerable", this.invulnerable);

        if (!this.effects.isEmpty()) {
            ListTag<CompoundTag> list = new ListTag<>("ActiveEffects");
            for (Effect effect : this.effects.values()) {
                list.add(new CompoundTag(String.valueOf(effect.getId()))
                        .putByte("Id", effect.getId())
                        .putByte("Amplifier", effect.getAmplifier())
                        .putInt("Duration", effect.getDuration())
                        .putBoolean("Ambient", false)
                        .putBoolean("ShowParticles", effect.isVisible()));
            }

            this.namedTag.putList(list);
        } else {
            this.namedTag.remove("ActiveEffects");
        }
    }

    public String getName() {
        if (this.hasCustomName()) {
            return this.getNameTag();
        } else {
            return this.getSaveId();
        }
    }

    public final String getSaveId() {
        return shortNames.getOrDefault(this.getClass().getSimpleName(), "");
    }

    public void spawnTo(ServerPlayer player) {
        if (!this.hasSpawned.containsKey(player.getLoaderId()) &&
                player.usedChunks.containsKey(Level.chunkHash(this.chunk.getX(), this.chunk.getZ()))) {
            this.hasSpawned.put(player.getLoaderId(), player);
        }
    }

    public Map<Integer, ServerPlayer> getViewers() {
        return hasSpawned;
    }

    public void sendPotionEffects(ServerPlayer player) {
        for (Effect effect : this.effects.values()) {
            MobEffectPacket pk = new MobEffectPacket();
            pk.eid = 0;
            pk.effectId = effect.getId();
            pk.amplifier = effect.getAmplifier();
            pk.particles = effect.isVisible();
            pk.duration = effect.getDuration();
            pk.eventId = MobEffectPacket.EVENT_ADD;

            player.dataPacket(pk);
        }
    }

    public void sendData(ServerPlayer player) {
        this.sendData(player, null);
    }

    public void sendData(ServerPlayer player, EntityMetadata data) {
        SetEntityDataPacket pk = new SetEntityDataPacket();
        pk.eid = player == this ? 0 : this.getId();
        pk.metadata = data == null ? this.dataProperties : data;

        player.dataPacket(pk);
    }

    public void sendData(ServerPlayer[] players) {
        this.sendData(players, null);
    }

    public void sendData(ServerPlayer[] players, EntityMetadata data) {
        SetEntityDataPacket pk = new SetEntityDataPacket();
        pk.eid = this.getId();
        pk.metadata = data == null ? this.dataProperties : data;

        Server.broadcastPacket(players, pk);
    }

    public void despawnFrom(ServerPlayer player) {
        if (this.hasSpawned.containsKey(player.getLoaderId())) {
            RemoveEntityPacket pk = new RemoveEntityPacket();
            pk.eid = this.getId();
            player.dataPacket(pk);
            this.hasSpawned.remove(player.getLoaderId());
        }
    }

    public void attack(EntityDamageEvent source) {
        if (hasEffect(Effect.FIRE_RESISTANCE) &&
                (source.getCause() == EntityDamageEvent.CAUSE_FIRE ||
                        source.getCause() == EntityDamageEvent.CAUSE_FIRE_TICK ||
                        source.getCause() == EntityDamageEvent.CAUSE_LAVA)) {
            source.setCancelled();
        }

        getServer().getPluginManager().callEvent(source);
        if (source.isCancelled()) {
            return;
        }
        setLastDamageCause(source);
        setHealth(getHealth() - source.getFinalDamage());
    }

    public void attack(float damage) {
        this.attack(new EntityDamageEvent(this, EntityDamageEvent.CAUSE_VOID, damage));
    }

    public void heal(EntityRegainHealthEvent source) {
        this.server.getPluginManager().callEvent(source);
        if (source.isCancelled()) {
            return;
        }
        this.setHealth(this.getHealth() + source.getAmount());
    }

    public void heal(float amount) {
        this.heal(new EntityRegainHealthEvent(this, amount, EntityRegainHealthEvent.CAUSE_REGEN));
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(float health) {
        int h = (int) health;
        if (this.health == h) {
            return;
        }

        if (h <= 0) {
            if (this.isAlive()) {
                this.kill();
            }
        } else if (h <= this.getMaxHealth() || h < this.health) {
            this.health = h;
        } else {
            this.health = this.getMaxHealth();
        }
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public EntityDamageEvent getLastDamageCause() {
        return lastDamageCause;
    }

    public void setLastDamageCause(EntityDamageEvent type) {
        this.lastDamageCause = type;
    }

    public int getMaxHealth() {
        return maxHealth + (this.hasEffect(Effect.HEALTH_BOOST)
                ? 4 * (this.getEffect(Effect.HEALTH_BOOST).getAmplifier() + 1)
                : 0);
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public boolean canCollideWith(Entity entity) {
        return !this.justCreated && this != entity;
    }

    protected boolean checkObstruction(double x, double y, double z) {
        int i = MathUtilities.floorDouble(x);
        int j = MathUtilities.floorDouble(y);
        int k = MathUtilities.floorDouble(z);

        double diffX = x - i;
        double diffY = y - j;
        double diffZ = z - k;

        if (!Block.transparent[this.level.getBlockIdAt(i, j, k)]) {
            boolean flag = Block.transparent[this.level.getBlockIdAt(i - 1, j, k)];
            boolean flag1 = Block.transparent[this.level.getBlockIdAt(i + 1, j, k)];
            boolean flag2 = Block.transparent[this.level.getBlockIdAt(i, j - 1, k)];
            boolean flag3 = Block.transparent[this.level.getBlockIdAt(i, j + 1, k)];
            boolean flag4 = Block.transparent[this.level.getBlockIdAt(i, j, k - 1)];
            boolean flag5 = Block.transparent[this.level.getBlockIdAt(i, j, k + 1)];

            int direction = -1;
            double limit = 9999;

            if (flag) {
                limit = diffX;
                direction = 0;
            }

            if (flag1 && 1 - diffX < limit) {
                limit = 1 - diffX;
                direction = 1;
            }

            if (flag2 && diffY < limit) {
                limit = diffY;
                direction = 2;
            }

            if (flag3 && 1 - diffY < limit) {
                limit = 1 - diffY;
                direction = 3;
            }

            if (flag4 && diffZ < limit) {
                limit = diffZ;
                direction = 4;
            }

            if (flag5 && 1 - diffZ < limit) {
                direction = 5;
            }

            double force = new Random().nextDouble() * 0.2 + 0.1;

            if (direction == 0) {
                this.motionX = -force;

                return true;
            }

            if (direction == 1) {
                this.motionX = force;

                return true;
            }

            if (direction == 2) {
                this.motionY = -force;

                return true;
            }

            if (direction == 3) {
                this.motionY = force;

                return true;
            }

            if (direction == 4) {
                this.motionZ = -force;

                return true;
            }

            if (direction == 5) {
                this.motionZ = force;

                return true;
            }
        }

        return false;
    }

    public boolean entityBaseTick() {
        return this.entityBaseTick(1);
    }

    public boolean entityBaseTick(int tickDiff) {
        this.blocksAround = null;
        this.justCreated = false;

        if (!this.isAlive()) {
            this.removeAllEffects();
            this.despawnFromAll();
            if (!this.isPlayer) {
                this.close();
            }
            return false;
        }

        if (!this.effects.isEmpty()) {
            for (Effect effect : this.effects.values()) {
                if (effect.canTick()) {
                    effect.applyEffect(this);
                }
                effect.setDuration(effect.getDuration() - tickDiff);

                if (effect.getDuration() <= 0) {
                    this.removeEffect(effect.getId());
                }
            }
        }

        boolean hasUpdate = false;

        this.checkBlockCollision();

        if (this.y <= -16 && this.isAlive()) {
            EntityDamageEvent ev = new EntityDamageEvent(this, EntityDamageEvent.CAUSE_VOID, 10);
            this.attack(ev);
            hasUpdate = true;
        }

        if (this.fireTicks > 0) {
            if (this.fireProof) {
                this.fireTicks -= 4 * tickDiff;
                if (this.fireTicks < 0) {
                    this.fireTicks = 0;
                }
            } else {
                if (!this.hasEffect(Effect.FIRE_RESISTANCE) &&
                        ((this.fireTicks % 20) == 0 || tickDiff > 20)) {
                    EntityDamageEvent ev = new EntityDamageEvent(this, EntityDamageEvent.CAUSE_FIRE_TICK, 1);
                    this.attack(ev);
                }
                this.fireTicks -= tickDiff;
            }
            if (this.fireTicks <= 0) {
                this.extinguish();
            } else {
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_ONFIRE, true);
                hasUpdate = true;
            }
        }

        if (this.noDamageTicks > 0) {
            this.noDamageTicks -= tickDiff;
            if (this.noDamageTicks < 0) {
                this.noDamageTicks = 0;
            }
        }

        this.age += tickDiff;
        this.ticksLived += tickDiff;

        return hasUpdate;
    }

    protected void updateMovement() {
        double diffPosition = (this.x - this.lastX) * (this.x - this.lastX) +
                (this.y - this.lastY) * (this.y - this.lastY) +
                (this.z - this.lastZ) * (this.z - this.lastZ);
        double diffRotation = (this.yaw - this.lastYaw) * (this.yaw - this.lastYaw) +
                (this.pitch - this.lastPitch) * (this.pitch - this.lastPitch);

        double diffMotion = (this.motionX - this.lastMotionX) * (this.motionX - this.lastMotionX) +
                (this.motionY - this.lastMotionY) * (this.motionY - this.lastMotionY) +
                (this.motionZ - this.lastMotionZ) * (this.motionZ - this.lastMotionZ);

        if (diffPosition > 0.04 ||
                diffRotation > 2.25 && (diffMotion > 0.0001 && this.getMotion().lengthSquared() <=
                        0.00001)) { // 0.2 ** 2, 1.5 ** 2
            this.lastX = this.x;
            this.lastY = this.y;
            this.lastZ = this.z;

            this.lastYaw = this.yaw;
            this.lastPitch = this.pitch;

            this.addMovement(this.x, this.y + this.getEyeHeight(), this.z, this.yaw, this.pitch,
                    this.yaw);
        }

        if (diffMotion > 0.0025 ||
                (diffMotion > 0.0001 && this.getMotion().lengthSquared() <= 0.0001)) { // 0.05 ** 2
            this.lastMotionX = this.motionX;
            this.lastMotionY = this.motionY;
            this.lastMotionZ = this.motionZ;

            this.addMotion(this.motionX, this.motionY, this.motionZ);
        }
    }

    public void addMovement(double x, double y, double z, double yaw, double pitch, double headYaw) {
        this.level.addEntityMovement(this.chunk.getX(), this.chunk.getZ(), this.id, x, y, z, yaw, pitch,
                headYaw);
    }

    public void addMotion(double motionX, double motionY, double motionZ) {
        this.level.addEntityMotion(this.chunk.getX(), this.chunk.getZ(), this.id, motionX, motionY,
                motionZ);
    }

    public Vector3 getDirectionVector() {
        Vector3 vector = super.getDirectionVector();
        return this.getTemporalVector().setComponents(vector.x, vector.y, vector.z);
    }

    public Vector2 getDirectionPlane() {
        return (new Vector2((float) (-Math.cos(Math.toRadians(this.yaw) - Math.PI / 2)),
                (float) (-Math.sin(Math.toRadians(this.yaw) - Math.PI / 2))))
                .normalize();
    }

    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (!this.isAlive()) {
            ++this.deadTicks;
            if (this.deadTicks >= 10) {
                this.despawnFromAll();
                if (!this.isPlayer) {
                    this.close();
                }
            }
            return this.deadTicks < 10;
        }

        int tickDiff = currentTick - this.lastUpdate;

        if (tickDiff <= 0) {
            return false;
        }

        this.lastUpdate = currentTick;

        boolean hasUpdate = this.entityBaseTick(tickDiff);

        this.updateMovement();

        return hasUpdate;
    }

    public final void scheduleUpdate() {
        this.level.updateEntities.put(this.id, this);
    }

    public boolean isOnFire() {
        return this.fireTicks > 0;
    }

    public void setOnFire(int seconds) {
        int ticks = seconds * 20;
        if (ticks > this.fireTicks) {
            this.fireTicks = ticks;
        }
    }

    public Integer getDirection() {
        double rotation = (this.yaw - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if ((0 <= rotation && rotation < 45) || (315 <= rotation && rotation < 360)) {
            return 2; // North
        } else if (45 <= rotation && rotation < 135) {
            return 3; // East
        } else if (135 <= rotation && rotation < 225) {
            return 0; // South
        } else if (225 <= rotation && rotation < 315) {
            return 1; // West
        } else {
            return null;
        }
    }

    public void extinguish() {
        this.fireTicks = 0;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_ONFIRE, false);
    }

    public boolean canTriggerWalking() {
        return true;
    }

    public void resetFallDistance() {
        this.highestPosition = 0;
    }

    protected void updateFallState(boolean onGround) {
        if (onGround) {
            fallDistance = (float) (this.highestPosition - this.y);

            if (fallDistance > 0) {
                if (this instanceof EntityLiving && !this.isInsideOfWater()) {
                    this.fall(fallDistance);
                }
                this.resetFallDistance();
            }
        }
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void fall(float fallDistance) {
        float damage = (float) Math.floor(
                fallDistance - 3 -
                        (this.hasEffect(Effect.JUMP) ? this.getEffect(Effect.JUMP).getAmplifier() + 1 : 0));
        if (damage > 0) {
            EntityDamageEvent ev = new EntityDamageEvent(this, EntityDamageEvent.CAUSE_FALL, damage);
            this.attack(ev);
        }

        if (fallDistance > 1) {
            Block down = this.level.getBlock(
                    this.getTemporalVector().setComponents(getFloorX(), getFloorY() - 1, getFloorZ()));

            if (down.getId() == Item.FARMLAND) {
                this.level.setBlock(this.getTemporalVector().setComponents(down.x, down.y, down.z),
                        new BlockDirt(), true, true);
            }
        }
    }

    public void handleLavaMovement() {
        // todo
    }

    public void moveFlying() {
        // todo
    }

    public void onCollideWithPlayer(EntityHuman entityPlayer) {
    }

    protected boolean switchLevel(Level targetLevel) {
        if (this.closed) {
            return false;
        }

        if (this.isValid()) {
            EntityLevelChangeEvent ev = new EntityLevelChangeEvent(this, this.level, targetLevel);
            this.server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return false;
            }

            this.level.removeEntity(this);
            if (this.chunk != null) {
                this.chunk.removeEntity(this);
            }
            this.despawnFromAll();
        }

        this.setLevel(targetLevel);
        this.level.addEntity(this);
        this.chunk = null;

        return true;
    }

    public Position getPosition() {
        return new Position(this.x, this.y, this.z, this.level);
    }

    public Location getLocation() {
        return new Location(this.x, this.y, this.z, this.yaw, this.pitch, this.level);
    }

    public boolean isInsideOfWater() {
        double y = this.y + this.getEyeHeight();
        Block block = this.level.getBlock(this.getTemporalVector().setComponents(
                MathUtilities.floorDouble(this.x), MathUtilities.floorDouble(y), MathUtilities.floorDouble(this.z)));

        if (block instanceof BlockWater) {
            double f = (block.y + 1) - (((BlockWater) block).getFluidHeightPercent() - 0.1111111);
            return y < f;
        }

        return false;
    }

    public boolean isInsideOfSolid() {
        double y = this.y + this.getEyeHeight();
        Block block = this.level.getBlock(this.getTemporalVector().setComponents(
                MathUtilities.floorDouble(this.x), MathUtilities.floorDouble(y), MathUtilities.floorDouble(this.z)));

        AxisAlignedBB bb = block.getBoundingBox();

        return bb != null && block.isSolid() && !block.isTransparent() &&
                bb.intersectsWith(this.getBoundingBox());
    }

    public boolean isInsideOfFire() {
        for (Block block : this.getBlocksAround()) {
            if (block instanceof BlockFire) {
                return true;
            }
        }

        return false;
    }

    public void fastMove(double dx, double dy, double dz) {
        if (dx == 0 && dy == 0 && dz == 0) {
            return;
        }

        AxisAlignedBB newBB = this.boundingBox.getOffsetBoundingBox(dx, dy, dz);

        if (server.getAllowFlight() || !this.level.hasCollision(this, newBB, false)) {
            this.boundingBox = newBB;
        }

        this.x = (this.boundingBox.minX + this.boundingBox.maxX) / 2;
        this.y = this.boundingBox.minY - this.ySize;
        this.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2;

        this.checkChunks();

        if (!this.onGround || dy != 0) {
            AxisAlignedBB bb = this.boundingBox.clone();
            bb.minY -= 0.75;

            this.onGround = this.level.getCollisionBlocks(bb).length > 0;
        }

        this.isCollided = this.onGround;
        this.updateFallState(this.onGround);

    }

    public boolean move(double dx, double dy, double dz) {

        if (dx == 0 && dz == 0 && dy == 0) {
            return true;
        }

        if (this.keepMovement) {
            this.boundingBox.offset(dx, dy, dz);
            this.setPosition(this.getTemporalVector().setComponents(
                    (this.boundingBox.minX + this.boundingBox.maxX) / 2, this.boundingBox.minY,
                    (this.boundingBox.minZ + this.boundingBox.maxZ) / 2));
            this.onGround = this.isPlayer;
            return true;
        } else {

            this.ySize *= 0.4;

            double movX = dx;
            double movY = dy;
            double movZ = dz;

            AxisAlignedBB axisalignedbb = this.boundingBox.clone();

            AxisAlignedBB[] list = this.level.getCollisionCubes(
                    this,
                    this.level.getTickRate() > 1 ? this.boundingBox.getOffsetBoundingBox(dx, dy, dz)
                            : this.boundingBox.addCoord(dx, dy, dz),
                    false);

            for (AxisAlignedBB bb : list) {
                dy = bb.calculateYOffset(this.boundingBox, dy);
            }

            this.boundingBox.offset(0, dy, 0);

            boolean fallingFlag = (this.onGround || (dy != movY && movY < 0));

            for (AxisAlignedBB bb : list) {
                dx = bb.calculateXOffset(this.boundingBox, dx);
            }

            this.boundingBox.offset(dx, 0, 0);

            for (AxisAlignedBB bb : list) {
                dz = bb.calculateZOffset(this.boundingBox, dz);
            }

            this.boundingBox.offset(0, 0, dz);

            if (this.getStepHeight() > 0 && fallingFlag && this.ySize < 0.05 &&
                    (movX != dx || movZ != dz)) {
                double cx = dx;
                double cy = dy;
                double cz = dz;
                dx = movX;
                dy = this.getStepHeight();
                dz = movZ;

                AxisAlignedBB axisalignedbb1 = this.boundingBox.clone();

                this.boundingBox.setBB(axisalignedbb);

                list = this.level.getCollisionCubes(this, this.boundingBox.addCoord(dx, dy, dz), false);

                for (AxisAlignedBB bb : list) {
                    dy = bb.calculateYOffset(this.boundingBox, dy);
                }

                this.boundingBox.offset(0, dy, 0);

                for (AxisAlignedBB bb : list) {
                    dx = bb.calculateXOffset(this.boundingBox, dx);
                }

                this.boundingBox.offset(dx, 0, 0);

                for (AxisAlignedBB bb : list) {
                    dz = bb.calculateZOffset(this.boundingBox, dz);
                }

                this.boundingBox.offset(0, 0, dz);

                this.boundingBox.offset(0, 0, dz);

                if ((cx * cx + cz * cz) >= (dx * dx + dz * dz)) {
                    dx = cx;
                    dy = cy;
                    dz = cz;
                    this.boundingBox.setBB(axisalignedbb1);
                } else {
                    this.ySize += 0.5;
                }
            }

            this.x = (this.boundingBox.minX + this.boundingBox.maxX) / 2;
            this.y = this.boundingBox.minY - this.ySize;
            this.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2;

            this.checkChunks();

            this.checkGroundState(movX, movY, movZ, dx, dy, dz);
            this.updateFallState(this.onGround);

            if (movX != dx) {
                this.motionX = 0;
            }

            if (movY != dy) {
                this.motionY = 0;
            }

            if (movZ != dz) {
                this.motionZ = 0;
            }

            // TODO: vehicle collision events (first we need to spawn them!)
            return true;
        }
    }

    protected void checkGroundState(double movX, double movY, double movZ, double dx, double dy,
                                    double dz) {
        this.isCollidedVertically = movY != dy;
        this.isCollidedHorizontally = (movX != dx || movZ != dz);
        this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically);
        this.onGround = (movY != dy && movY < 0);
    }

    public List<Block> getBlocksAround() {
        if (this.blocksAround == null) {
            int minX = MathUtilities.floorDouble(this.boundingBox.minX);
            int minY = MathUtilities.floorDouble(this.boundingBox.minY);
            int minZ = MathUtilities.floorDouble(this.boundingBox.minZ);
            int maxX = MathUtilities.ceilDouble(this.boundingBox.maxX);
            int maxY = MathUtilities.ceilDouble(this.boundingBox.maxY);
            int maxZ = MathUtilities.ceilDouble(this.boundingBox.maxZ);

            this.blocksAround = new ArrayList<>();

            for (int z = minZ; z <= maxZ; ++z) {
                for (int x = minX; x <= maxX; ++x) {
                    for (int y = minY; y <= maxY; ++y) {
                        Block block = this.level.getBlock(this.getTemporalVector().setComponents(x, y, z));
                        if (block.hasEntityCollision()) {
                            this.blocksAround.add(block);
                        }
                    }
                }
            }
        }

        return this.blocksAround;
    }

    protected void checkBlockCollision() {
        Vector3 vector = new Vector3(0, 0, 0);

        for (Block block : this.getBlocksAround()) {
            block.onEntityCollide(this);
            block.addVelocityToEntity(this, vector);
        }

        if (vector.lengthSquared() > 0) {
            vector = vector.normalize();
            double d = 0.014d;
            this.motionX += vector.x * d;
            this.motionY += vector.y * d;
            this.motionZ += vector.z * d;
        }
    }

    public boolean setPositionAndRotation(Vector3 pos, double yaw, double pitch) {
        if (this.setPosition(pos)) {
            this.setRotation(yaw, pitch);
            return true;
        }

        return false;
    }

    public void setRotation(double yaw, double pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.scheduleUpdate();
    }

    protected void checkChunks() {
        var chunk = this.chunk;

        var cx = (int) this.x >> 4;
        var cz = (int) this.z >> 4;

        if (chunk == null || chunk.getX() != cx || this.chunk.getZ() != cz) {
            if (chunk != null) {
                chunk.removeEntity(this);
            }

            this.chunk = level.getChunk(cx, cz);

            if (!this.justCreated) {
                Map<Integer, ServerPlayer> newChunk =
                        this.level.getChunkPlayers((int) this.x >> 4, (int) this.z >> 4);
                for (ServerPlayer player : new ArrayList<>(this.hasSpawned.values())) {
                    if (!newChunk.containsKey(player.getLoaderId())) {
                        this.despawnFrom(player);
                    } else {
                        newChunk.remove(player.getLoaderId());
                    }
                }

                for (ServerPlayer player : newChunk.values()) {
                    this.spawnTo(player);
                }
            }

            if (this.chunk == null) {
                return;
            }

            this.chunk.addEntity(this);
        }
    }

    public boolean setPosition(Vector3 pos) {
        if (this.closed) {
            return false;
        }

        if (pos instanceof Position && ((Position) pos).level != null &&
                ((Position) pos).level != this.level) {
            if (!this.switchLevel(((Position) pos).getLevel())) {
                return false;
            }
        }

        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;

        double radius = this.getWidth() / 2d;

        this.boundingBox.setBounds(pos.x - radius, pos.y, pos.z - radius, pos.x + radius,
                pos.y + this.getHeight(), pos.z + radius);

        this.checkChunks();

        return true;
    }

    public Vector3 getMotion() {
        return new Vector3(this.motionX, this.motionY, this.motionZ);
    }

    public boolean setMotion(Vector3 motion) {
        if (!this.justCreated) {
            EntityMotionEvent ev = new EntityMotionEvent(this, motion);
            this.server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return false;
            }
        }

        this.motionX = motion.x;
        this.motionY = motion.y;
        this.motionZ = motion.z;

        if (!this.justCreated) {
            this.updateMovement();
        }

        return true;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void kill() {
        this.health = 0;
        this.scheduleUpdate();
    }

    public boolean teleport(Vector3 pos) {
        return this.teleport(pos, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public boolean teleport(Vector3 pos, PlayerTeleportEvent.TeleportCause cause) {
        return this.teleport(Location.fromObject(pos, this.level, this.yaw, this.pitch), cause);
    }

    public boolean teleport(Position pos) {
        return this.teleport(pos, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public boolean teleport(Position pos, PlayerTeleportEvent.TeleportCause cause) {
        return this.teleport(Location.fromObject(pos, pos.level, this.yaw, this.pitch), cause);
    }

    public boolean teleport(Location location) {
        return this.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        double yaw = location.yaw;
        double pitch = location.pitch;

        Location from = this.getLocation();
        Location to = location;
        if (cause != null) {
            EntityTeleportEvent ev = new EntityTeleportEvent(this, from, to);
            this.server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return false;
            }
            to = ev.getTo();
        }

        this.ySize = 0;

        this.setMotion(this.getTemporalVector().setComponents(0, 0, 0));

        if (this.setPositionAndRotation(to, yaw, pitch)) {
            this.resetFallDistance();
            this.onGround = true;

            this.updateMovement();

            return true;
        }

        return false;
    }

    public long getId() {
        return this.id;
    }

    public void respawnToAll() {
        for (ServerPlayer player : this.hasSpawned.values()) {
            this.spawnTo(player);
        }
        this.hasSpawned = new HashMap<>();
    }

    public void spawnToAll() {
        if (this.chunk == null || this.closed) {
            return;
        }

        for (ServerPlayer player :
                this.level.getChunkPlayers(this.chunk.getX(), this.chunk.getZ()).values()) {
            if (player.isOnline()) {
                this.spawnTo(player);
            }
        }
    }

    public void despawnFromAll() {
        for (ServerPlayer player : new ArrayList<>(this.hasSpawned.values())) {
            this.despawnFrom(player);
        }
    }

    public void close() {
        if (!this.closed) {
            this.server.getPluginManager().callEvent(new EntityDespawnEvent(this));
            this.closed = true;
            this.despawnFromAll();
            if (this.chunk != null) {
                this.chunk.removeEntity(this);
            }

            if (this.level != null) {
                this.level.removeEntity(this);
            }
        }
    }

    public boolean setDataProperty(EntityData data) {
        return setDataProperty(data, true);
    }

    public boolean setDataProperty(EntityData data, boolean send) {
        if (!Objects.equals(data, this.getDataProperties().get(data.getId()))) {
            this.getDataProperties().put(data);
            if (send)
                this.sendData(this.hasSpawned.values().stream().toArray(ServerPlayer[]::new),
                        new EntityMetadata().put(this.dataProperties.get(data.getId())));
            return true;
        }
        return false;
    }

    public EntityMetadata getDataProperties() {
        return this.dataProperties;
    }

    public EntityData getDataProperty(int id) {
        return this.getDataProperties().get(id);
    }

    public int getDataPropertyInt(int id) {
        return this.getDataProperties().getInt(id);
    }

    public int getDataPropertyShort(int id) {
        return this.getDataProperties().getShort(id);
    }

    public int getDataPropertyByte(int id) {
        return this.getDataProperties().getByte(id);
    }

    public boolean getDataPropertyBoolean(int id) {
        return this.getDataProperties().getBoolean(id);
    }

    public long getDataPropertyLong(int id) {
        return this.getDataProperties().getLong(id);
    }

    public String getDataPropertyString(int id) {
        return this.getDataProperties().getString(id);
    }

    public float getDataPropertyFloat(int id) {
        return this.getDataProperties().getFloat(id);
    }

    public Item getDataPropertySlot(int id) {
        return this.getDataProperties().getSlot(id);
    }

    public Vector3 getDataPropertyPos(int id) {
        return this.getDataProperties().getPosition(id);
    }

    public int getDataPropertyType(int id) {
        return this.getDataProperties().exists(id) ? this.getDataProperty(id).getType() : -1;
    }

    public void setDataFlag(int propertyId, int id) {
        this.setDataFlag(propertyId, id, true);
    }

    public void setDataFlag(int propertyId, int id, boolean value) {
        if (this.getDataFlag(propertyId, id) != value) {
            int flags = this.getDataPropertyByte(propertyId);
            flags ^= 1 << id;
            this.setDataProperty(new ByteEntityData(propertyId, flags));
        }
    }

    public boolean getDataFlag(int propertyId, int id) {
        return ((this.getDataPropertyByte(propertyId) & 0xff) & (1 << id)) > 0;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.server.getEntityMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.server.getEntityMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    public Server getServer() {
        return server;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Entity other = (Entity) obj;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (int) (29 * hash + this.getId());
        return hash;
    }

    public Vector3 getTemporalVector() {
        return temporalVector;
    }

    public void setTemporalVector(Vector3 temporalVector) {
        this.temporalVector = temporalVector;
    }

    public Map<Integer, ServerPlayer> getHasSpawned() {
        return hasSpawned;
    }
}
