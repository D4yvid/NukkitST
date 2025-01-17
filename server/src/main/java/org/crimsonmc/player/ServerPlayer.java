package org.crimsonmc.player;

import lombok.Getter;
import lombok.Setter;
import org.crimsonmc.block.Block;
import org.crimsonmc.block.BlockAir;
import org.crimsonmc.blockentity.BlockEntity;
import org.crimsonmc.blockentity.BlockEntityItemFrame;
import org.crimsonmc.blockentity.BlockEntitySign;
import org.crimsonmc.blockentity.BlockEntitySpawnable;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.entity.Attribute;
import org.crimsonmc.entity.Entity;
import org.crimsonmc.entity.EntityHuman;
import org.crimsonmc.entity.EntityLiving;
import org.crimsonmc.entity.data.*;
import org.crimsonmc.entity.item.EntityItem;
import org.crimsonmc.entity.item.EntityVehicle;
import org.crimsonmc.entity.item.EntityXPOrb;
import org.crimsonmc.entity.projectile.EntityArrow;
import org.crimsonmc.event.block.ItemFrameDropItemEvent;
import org.crimsonmc.event.block.SignChangeEvent;
import org.crimsonmc.event.entity.*;
import org.crimsonmc.event.inventory.CraftItemEvent;
import org.crimsonmc.event.inventory.InventoryCloseEvent;
import org.crimsonmc.event.inventory.InventoryPickupArrowEvent;
import org.crimsonmc.event.inventory.InventoryPickupItemEvent;
import org.crimsonmc.event.player.*;
import org.crimsonmc.event.player.PlayerTeleportEvent.TeleportCause;
import org.crimsonmc.event.server.DataPacketReceiveEvent;
import org.crimsonmc.event.server.DataPacketSendEvent;
import org.crimsonmc.inventory.*;
import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemArrow;
import org.crimsonmc.item.ItemBlock;
import org.crimsonmc.item.ItemGlassBottle;
import org.crimsonmc.item.enchantment.Enchantment;
import org.crimsonmc.item.food.Food;
import org.crimsonmc.lang.TextContainer;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.level.ChunkLoader;
import org.crimsonmc.level.Level;
import org.crimsonmc.level.Location;
import org.crimsonmc.level.Position;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.level.format.generic.BaseFullChunk;
import org.crimsonmc.level.particle.CriticalParticle;
import org.crimsonmc.level.sound.ExperienceOrbSound;
import org.crimsonmc.level.sound.ItemFrameItemRemovedSound;
import org.crimsonmc.level.sound.LaunchSound;
import org.crimsonmc.math.*;
import org.crimsonmc.metadata.MetadataValue;
import org.crimsonmc.nbt.NBTIO;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.nbt.tag.DoubleTag;
import org.crimsonmc.nbt.tag.FloatTag;
import org.crimsonmc.nbt.tag.ListTag;
import org.crimsonmc.network.SourceInterface;
import org.crimsonmc.network.binary.Binary;
import org.crimsonmc.network.binary.ZlibCompression;
import org.crimsonmc.network.player.PacketHandler;
import org.crimsonmc.network.player.PlayerPacketHandler;
import org.crimsonmc.network.protocol.*;
import org.crimsonmc.permission.PermissibleBase;
import org.crimsonmc.permission.Permission;
import org.crimsonmc.permission.PermissionAttachment;
import org.crimsonmc.permission.PermissionAttachmentInfo;
import org.crimsonmc.plugin.Plugin;
import org.crimsonmc.potion.Effect;
import org.crimsonmc.potion.Potion;
import org.crimsonmc.server.Server;
import org.crimsonmc.text.TextFormat;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * author: MagicDroidX & Box crimsonmc Project
 */
public class ServerPlayer
        extends EntityHuman implements CommandSender, InventoryHolder, ChunkLoader, Player {

    public static final int SURVIVAL = 0;

    public static final int CREATIVE = 1;

    public static final int ADVENTURE = 2;

    public static final int SPECTATOR = 3;

    public static final int VIEW = SPECTATOR;

    public static final int SURVIVAL_SLOTS = 36;

    public static final int CRAFTING_SMALL = 0;

    public static final int CRAFTING_BIG = 1;

    public static final int CRAFTING_ANVIL = 2;

    protected final SourceInterface interfaz;

    protected final String ip;

    @Getter
    protected final int port;

    protected final int chunksPerTick;

    protected final int spawnThreshold;
    protected final Long clientID;
    protected final int viewDistance;
    private final Map<Integer, Boolean> needACK = new HashMap<>();
    private final AtomicReference<Locale> locale = new AtomicReference<>(null);
    public boolean playedBefore;
    public boolean loggedIn = false;
    @Getter
    public int gamemode;
    public long lastBreak;
    public Vector3 speed = null;

    // todo: achievements
    public boolean blocked = false;
    public int craftingType = CRAFTING_SMALL;
    public long creationTime = 0;
    public Map<Long, Boolean> usedChunks = new HashMap<>();
    protected int windowCnt = 2;
    protected Map<Inventory, Integer> windows;
    protected Map<Integer, Inventory> windowIndex = new HashMap<>();
    protected int messageCounter = 2;
    protected SimpleTransactionGroup currentTransaction = null;
    @Getter
    protected long randomClientId = -1;
    protected Vector3 forceMovement = null;
    @Getter
    protected boolean connected = true;

    @Setter
    protected boolean removeFormat = true;

    @Getter
    protected String username;

    @Getter
    protected String displayName;
    protected Vector3 sleeping = null;
    protected int chunkLoadCount = 0;
    protected Map<Long, Integer> loadQueue = new HashMap<>();
    protected int nextChunkOrderRun = 5;
    protected Map<UUID, ServerPlayer> hiddenPlayers = new HashMap<>();
    protected Vector3 newPosition = null;
    protected int chunkRadius;
    protected Position spawnPosition = null;
    @Getter
    protected int inAirTicks = 0;
    protected int startAirTicks = 5;
    @Getter
    protected AdventureSettings adventureSettings;
    @Getter
    protected PlayerFood foodData = null;
    private boolean spawned = false;
    private Vector3 teleportPosition = null;
    private int startAction = -1;
    @Getter
    private String clientSecret;

    private Integer loaderId = null;

    private Map<Integer, List<DataPacket>> batchedPackets = new TreeMap<>();

    private PermissibleBase perm;

    private int exp = 0;

    private int expLevel = 0;

    @Getter
    private Entity killer = null;

    private int hash;

    @Setter
    private boolean foodEnabled = true;

    public ServerPlayer(SourceInterface interfaz, Long clientID, String ip, int port) {
        super(null, new CompoundTag());
        this.interfaz = interfaz;
        this.windows = new HashMap<>();
        this.perm = new PermissibleBase(this);
        this.server = Server.getInstance();
        this.lastBreak = Long.MAX_VALUE;
        this.ip = ip;
        this.port = port;
        this.clientID = clientID;
        this.loaderId = Level.generateChunkLoaderId(this);
        this.chunksPerTick = (int) this.server.getConfig("chunk-sending.per-tick", 4);
        this.spawnThreshold = (int) this.server.getConfig("chunk-sending.spawn-threshold", 56);
        this.spawnPosition = null;
        this.gamemode = this.server.getGamemode();
        this.setLevel(this.server.getDefaultLevel());
        this.viewDistance = this.server.getViewDistance();
        // this.newPosition = new Vector3(0, 0, 0);
        this.boundingBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

        this.setUuid(null);
        this.setRawUUID(null);

        this.creationTime = System.currentTimeMillis();
    }

    public static int calculateRequireExperience(int level) {
        if (level < 16) {
            return 2 * level + 7;
        } else if (level >= 17 && level <= 31) {
            return 5 * level - 38;
        } else {
            return 9 * level - 158;
        }
    }

    public static BatchPacket getChunkCacheFromData(int chunkX, int chunkZ, byte[] payload) {
        return getChunkCacheFromData(chunkX, chunkZ, payload, FullChunkDataPacket.ORDER_COLUMNS);
    }

    public static BatchPacket getChunkCacheFromData(int chunkX, int chunkZ, byte[] payload,
                                                    byte ordering) {
        FullChunkDataPacket pk = new FullChunkDataPacket();
        pk.chunkX = chunkX;
        pk.chunkZ = chunkZ;
        pk.order = ordering;
        pk.data = payload;
        pk.encode();

        BatchPacket batch = new BatchPacket();
        byte[][] batchPayload = new byte[2][];
        byte[] buf = pk.getBuffer();
        batchPayload[0] = Binary.writeInt(buf.length);
        batchPayload[1] = buf;
        byte[] data = Binary.appendBytes(batchPayload);
        try {
            batch.payload = ZlibCompression.deflate(data, Server.getInstance().networkCompressionLevel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        batch.encode();
        batch.isEncoded = true;
        return batch;
    }

    public TranslationContainer getLeaveMessage() {
        return new TranslationContainer(TextFormat.YELLOW + "%multiplayer.player.left",
                this.getDisplayName());
    }

    /**
     * This might disappear in the future. Please use getUniqueId() instead (IP + clientId + name
     * combo, in the future it'll change to real UUID for online auth)
     */
    @Deprecated
    public Long getClientId() {
        return randomClientId;
    }

    @Override
    public boolean isBanned() {
        return this.server.getNameBans().isBanned(this.getName().toLowerCase());
    }

    @Override
    public void setBanned(boolean value) {
        if (value) {
            this.server.getNameBans().addBan(this.getName(), null, null, null);
            this.kick("You have been banned");
        } else {
            this.server.getNameBans().remove(this.getName());
        }
    }

    @Override
    public boolean isWhitelisted() {
        return this.server.isWhitelisted(this.getName().toLowerCase());
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            this.server.addWhitelist(this.getName().toLowerCase());
        } else {
            this.server.removeWhitelist(this.getName().toLowerCase());
        }
    }

    @Override
    public ServerPlayer getPlayer() {
        return this;
    }

    @Override
    public Long getFirstPlayed() {
        return this.namedTag != null ? this.namedTag.getLong("firstPlayed") : null;
    }

    @Override
    public Long getLastPlayed() {
        return this.namedTag != null ? this.namedTag.getLong("lastPlayed") : null;
    }

    @Override
    public boolean hasPlayedBefore() {
        return this.playedBefore;
    }

    public void setAdventureSettings(AdventureSettings adventureSettings) {
        this.adventureSettings = adventureSettings.clone(this);
        this.adventureSettings.update();
    }

    public void resetInAirTicks() {
        this.inAirTicks = 0;
    }

    @Deprecated
    public boolean getAllowFlight() {
        return this.getAdventureSettings().canFly();
    }

    @Deprecated
    public void setAllowFlight(boolean value) {
        this.getAdventureSettings().setCanFly(value);
        this.getAdventureSettings().update();
    }

    @Deprecated
    public void setAutoJump(boolean value) {
        this.getAdventureSettings().setAutoJump(value);
        this.getAdventureSettings().update();
    }

    @Deprecated
    public boolean hasAutoJump() {
        return this.getAdventureSettings().isAutoJumpEnabled();
    }

    @Override
    public void spawnTo(ServerPlayer player) {
        if (this.isSpawned() && player.isSpawned() && this.isAlive() && player.isAlive() &&
                player.getLevel() == this.level && player.canSee(this) && !this.isSpectator()) {
            super.spawnTo(player);
        }
    }

    public boolean getRemoveFormat() {
        return removeFormat;
    }

    public void setRemoveFormat() {
        this.setRemoveFormat(true);
    }

    public boolean canSee(ServerPlayer player) {
        return !this.hiddenPlayers.containsKey(player.getUniqueId());
    }

    public void hidePlayer(ServerPlayer player) {
        if (this == player) {
            return;
        }
        this.hiddenPlayers.put(player.getUniqueId(), player);
        player.despawnFrom(this);
    }

    public void showPlayer(ServerPlayer player) {
        if (this == player) {
            return;
        }
        this.hiddenPlayers.remove(player.getUniqueId());
        if (player.isOnline()) {
            player.spawnTo(this);
        }
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public void resetFallDistance() {
        super.resetFallDistance();
        if (this.inAirTicks != 0) {
            this.startAirTicks = 5;
        }
        this.inAirTicks = 0;
        this.highestPosition = this.y;
    }

    @Override
    public boolean isOnline() {
        return this.connected && this.loggedIn;
    }

    @Override
    public boolean isOp() {
        return this.server.isOp(this.getName());
    }

    @Override
    public void setOp(boolean value) {
        if (value == this.isOp()) {
            return;
        }

        if (value) {
            this.server.addOp(this.getName());
        } else {
            this.server.removeOp(this.getName());
        }

        this.recalculatePermissions();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return this.perm.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return this.perm.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(String name) {
        return this.perm != null && this.perm.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return this.perm.hasPermission(permission);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.addAttachment(plugin, null);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name) {
        return this.addAttachment(plugin, name, null);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, Boolean value) {
        return this.perm.addAttachment(plugin, name, value);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        this.server.getPluginManager().unsubscribeFromPermission(Server.BROADCAST_CHANNEL_USERS, this);
        this.server.getPluginManager().unsubscribeFromPermission(
                Server.BROADCAST_CHANNEL_ADMINISTRATIVE, this);

        if (this.perm == null) {
            return;
        }

        this.perm.recalculatePermissions();

        if (this.hasPermission(Server.BROADCAST_CHANNEL_USERS)) {
            this.server.getPluginManager().subscribeToPermission(Server.BROADCAST_CHANNEL_USERS, this);
        }

        if (this.hasPermission(Server.BROADCAST_CHANNEL_ADMINISTRATIVE)) {
            this.server.getPluginManager().subscribeToPermission(Server.BROADCAST_CHANNEL_ADMINISTRATIVE,
                    this);
        }
    }

    @Override
    public Map<String, PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }

    public boolean isPlayer() {
        return true;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        if (this.isSpawned()) {
            this.server.updatePlayerListData(this.getUniqueId(), this.getId(), this.getDisplayName(),
                    this.getSkin());
        }
    }

    @Override
    public void setSkin(Skin skin) {
        super.setSkin(skin);
        if (this.isSpawned()) {
            this.server.updatePlayerListData(this.getUniqueId(), this.getId(), this.getDisplayName(),
                    skin);
        }
    }

    public String getAddress() {
        return this.ip;
    }

    public Position getNextPosition() {
        return this.newPosition != null
                ? new Position(this.newPosition.x, this.newPosition.y, this.newPosition.z, this.level)
                : this.getPosition();
    }

    public boolean isSleeping() {
        return this.sleeping != null;
    }

    @Override
    protected boolean switchLevel(Level targetLevel) {
        Level oldLevel = this.level;
        if (super.switchLevel(targetLevel)) {
            for (long index : new ArrayList<>(this.usedChunks.keySet())) {
                int chunkX = Level.getHashX(index);
                int chunkZ = Level.getHashZ(index);
                this.unloadChunk(chunkX, chunkZ, oldLevel);
            }

            this.usedChunks = new HashMap<>();
            SetTimePacket pk = new SetTimePacket();
            pk.time = this.level.getTime();
            pk.started = !this.level.stopTime;
            this.dataPacket(pk);
            return true;
        }
        return false;
    }

    public void unloadChunk(int x, int z) {
        this.unloadChunk(x, z, null);
    }

    public void unloadChunk(int x, int z, Level level) {
        level = level == null ? this.level : level;
        long index = Level.chunkHash(x, z);
        if (this.usedChunks.containsKey(index)) {
            for (Entity entity : level.getChunkEntities(x, z).values()) {
                if (entity != this) {
                    entity.despawnFrom(this);
                }
            }

            this.usedChunks.remove(index);
        }
        level.unregisterChunkLoader(this, x, z);
        this.loadQueue.remove(index);
    }

    public Position getSpawn() {
        if (this.spawnPosition != null && this.spawnPosition.getLevel() != null) {
            return this.spawnPosition;
        } else {
            return this.server.getDefaultLevel().getSafeSpawn();
        }
    }

    public void setSpawn(Vector3 pos) {
        Level level;
        if (!(pos instanceof Position)) {
            level = this.level;
        } else {
            level = ((Position) pos).getLevel();
        }
        this.spawnPosition = new Position(pos.x, pos.y, pos.z, level);
        SetSpawnPositionPacket pk = new SetSpawnPositionPacket();
        pk.x = (int) this.spawnPosition.x;
        pk.y = (int) this.spawnPosition.y;
        pk.z = (int) this.spawnPosition.z;
        this.dataPacket(pk);
    }

    public void sendChunk(int x, int z, DataPacket packet) {
        if (!this.connected) {
            return;
        }

        this.usedChunks.put(Level.chunkHash(x, z), true);
        this.chunkLoadCount++;

        this.dataPacket(packet);

        if (this.isSpawned()) {
            for (Entity entity : this.level.getChunkEntities(x, z).values()) {
                if (this != entity && !entity.closed && entity.isAlive()) {
                    entity.spawnTo(this);
                }
            }
        }
    }

    public void sendChunk(int x, int z, byte[] payload) {
        this.sendChunk(x, z, payload, FullChunkDataPacket.ORDER_COLUMNS);
    }

    public void sendChunk(int x, int z, byte[] payload, byte ordering) {
        if (!this.connected) {
            return;
        }

        this.usedChunks.put(Level.chunkHash(x, z), true);
        this.chunkLoadCount++;

        FullChunkDataPacket pk = new FullChunkDataPacket();
        pk.chunkX = x;
        pk.chunkZ = z;
        pk.order = ordering;
        pk.data = payload;

        this.batchDataPacket(pk);

        if (this.isSpawned()) {
            for (Entity entity : this.level.getChunkEntities(x, z).values()) {
                if (this != entity && !entity.closed && entity.isAlive()) {
                    entity.spawnTo(this);
                }
            }
        }
    }

    protected void sendNextChunk() {
        if (!this.connected) {
            return;
        }

        int count = 0;

        List<Map.Entry<Long, Integer>> entryList = new ArrayList<>(this.loadQueue.entrySet());
        entryList.sort((o1, o2) -> o1.getValue() - o2.getValue());

        for (Map.Entry<Long, Integer> entry : entryList) {
            long index = entry.getKey();
            if (count >= this.chunksPerTick) {
                break;
            }
            int chunkX = Level.getHashX(index);
            int chunkZ = Level.getHashZ(index);

            ++count;

            this.usedChunks.put(index, false);
            this.level.registerChunkLoader(this, chunkX, chunkZ, false);

            if (!this.level.populateChunk(chunkX, chunkZ)) {
                if (this.isSpawned() && this.getTeleportPosition() == null) {
                    continue;
                } else {
                    break;
                }
            }

            this.loadQueue.remove(index);

            PlayerChunkRequestEvent ev = new PlayerChunkRequestEvent(this, chunkX, chunkZ);
            this.server.getPluginManager().callEvent(ev);
            if (!ev.isCancelled()) {
                this.level.requestChunk(chunkX, chunkZ, this);
            }
        }
        if (this.chunkLoadCount >= this.spawnThreshold && !this.isSpawned() &&
                this.getTeleportPosition() == null) {
            this.doFirstSpawn();
        }
    }

    protected void doFirstSpawn() {
        this.spawned = true;

        this.server.sendRecipeList(this);
        this.getAdventureSettings().update();

        this.server.updatePlayerListData(this.getUniqueId(), this.getId(), this.getDisplayName(),
                this.getSkin());
        this.server.sendFullPlayerListData(this, false);

        this.sendPotionEffects(this);
        this.sendData(this);
        this.inventory.sendContents(this);
        this.inventory.sendArmorContents(this);

        SetTimePacket setTimePacket = new SetTimePacket();
        setTimePacket.time = this.level.getTime();
        setTimePacket.started = !this.level.stopTime;
        this.dataPacket(setTimePacket);

        Position pos = this.level.getSafeSpawn(this);

        PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(this, pos);

        this.server.getPluginManager().callEvent(respawnEvent);

        pos = respawnEvent.getRespawnPosition();

        RespawnPacket respawnPacket = new RespawnPacket();
        respawnPacket.x = (float) pos.x;
        respawnPacket.y = (float) pos.y;
        respawnPacket.z = (float) pos.z;
        this.dataPacket(respawnPacket);

        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.status = PlayStatusPacket.PLAYER_SPAWN;
        this.dataPacket(playStatusPacket);

        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(
                this, new TranslationContainer(TextFormat.YELLOW + "%multiplayer.player.joined",
                new String[]{this.getDisplayName()}));

        this.server.getPluginManager().callEvent(playerJoinEvent);

        if (!playerJoinEvent.getJoinMessage().toString().trim().isEmpty()) {
            this.server.broadcastMessage(playerJoinEvent.getJoinMessage());
        }

        this.noDamageTicks = 60;

        for (long index : this.usedChunks.keySet()) {
            int chunkX = Level.getHashX(index);
            int chunkZ = Level.getHashZ(index);
            for (Entity entity : this.level.getChunkEntities(chunkX, chunkZ).values()) {
                if (this != entity && !entity.closed && entity.isAlive()) {
                    entity.spawnTo(this);
                }
            }
        }

        this.sendExperience(this.getExperience());
        this.sendExperienceLevel(this.getExperienceLevel());

        this.teleport(pos, null); // Prevent PlayerTeleportEvent during player spawn

        if (!this.isSpectator()) {
            this.spawnToAll();
        }

        // todo Updater

        if (this.getHealth() <= 0) {
            respawnPacket = new RespawnPacket();
            pos = this.getSpawn();
            respawnPacket.x = (float) pos.x;
            respawnPacket.y = (float) pos.y;
            respawnPacket.z = (float) pos.z;
            this.dataPacket(respawnPacket);
        }

        // Weather
        this.getLevel().sendWeather(this);

        // FoodLevel
        this.getFoodData().sendFoodLevel();
    }

    protected void orderChunks() {
        if (!this.connected) {
            return;
        }

        this.nextChunkOrderRun = 200;

        Map<Long, Integer> newOrder = new HashMap<>();
        Map<Long, Boolean> lastChunk = new HashMap<>(this.usedChunks);

        int centerX = (int) this.x >> 4;
        int centerZ = (int) this.z >> 4;
        int count = 0;

        for (int x = -this.chunkRadius; x <= this.chunkRadius; x++) {
            for (int z = -this.chunkRadius; z <= this.chunkRadius; z++) {
                int chunkX = x + centerX;
                int chunkZ = z + centerZ;
                int distance = (int) Math.sqrt((double) x * x + (double) z * z);
                if (distance <= this.chunkRadius) {
                    long index;
                    if (!(this.usedChunks.containsKey(index = Level.chunkHash(chunkX, chunkZ))) ||
                            !this.usedChunks.get(index)) {
                        newOrder.put(index, distance);
                        count++;
                    }
                    lastChunk.remove(index);
                }
            }
        }

        for (long index : new ArrayList<>(lastChunk.keySet())) {
            this.unloadChunk(Level.getHashX(index), Level.getHashZ(index));
        }

        this.loadQueue = newOrder;
    }

    public void batchDataPacket(DataPacket packet) {
        if (!this.connected) {
            return;
        }

        DataPacketSendEvent event = new DataPacketSendEvent(this, packet);
        this.server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        if (!this.batchedPackets.containsKey(packet.getChannel())) {
            this.batchedPackets.put(packet.getChannel(), new ArrayList<>());
        }

        this.batchedPackets.get(packet.getChannel()).add(packet.clone());
    }

    /**
     * 0 is true -1 is false other is identifer
     */
    public boolean dataPacket(DataPacket packet) {
        return this.dataPacket(packet, false) != -1;
    }

    public int dataPacket(DataPacket packet, boolean needACK) {
        if (!this.connected) {
            return -1;
        }

        DataPacketSendEvent ev = new DataPacketSendEvent(this, packet);
        this.server.getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            return -1;
        }

        Integer identifier = this.interfaz.putPacket(this, packet, needACK, false);

        if (needACK && identifier != null) {
            this.needACK.put(identifier, false);
            return identifier;
        }
        return 0;
    }

    /**
     * 0 is true -1 is false other is identifer
     */
    public void directDataPacket(DataPacket packet) {
        this.directDataPacket(packet, false);
    }

    public void directDataPacket(DataPacket packet, boolean needACK) {
        if (!this.connected) {
            return;
        }

        DataPacketSendEvent ev = new DataPacketSendEvent(this, packet);
        this.server.getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            return;
        }

        Integer identifier = this.interfaz.putPacket(this, packet, needACK, true);

        if (needACK && identifier != null) {
            this.needACK.put(identifier, false);
        }
    }

    public int getPing() {
        return this.interfaz.getNetworkLatency(this);
    }

    public boolean sleepOn(Vector3 pos) {
        if (!this.isOnline()) {
            return false;
        }

        for (Entity p : this.level.getNearbyEntities(this.boundingBox.grow(2, 1, 2), this)) {
            if (p instanceof ServerPlayer) {
                if (((ServerPlayer) p).sleeping != null && pos.distance(((ServerPlayer) p).sleeping) <= 0.1) {
                    return false;
                }
            }
        }

        PlayerBedEnterEvent ev;
        this.server.getPluginManager().callEvent(
                ev = new PlayerBedEnterEvent(this, this.level.getBlock(pos)));
        if (ev.isCancelled()) {
            return false;
        }

        this.sleeping = pos.clone();
        this.teleport(
                new Location(pos.x + 0.5, pos.y - 0.5, pos.z + 0.5, this.yaw, this.pitch, this.level),
                null);

        this.setDataProperty(
                new PositionEntityData(DATA_PLAYER_BED_POSITION, (int) pos.x, (int) pos.y, (int) pos.z));
        this.setDataFlag(DATA_PLAYER_FLAGS, DATA_PLAYER_FLAG_SLEEP, true);

        this.setSpawn(pos);

        this.level.sleepTicks = 60;

        return true;
    }

    public void stopSleep() {
        if (this.sleeping != null) {
            this.server.getPluginManager().callEvent(
                    new PlayerBedLeaveEvent(this, this.level.getBlock(this.sleeping)));

            this.sleeping = null;
            this.setDataProperty(new PositionEntityData(DATA_PLAYER_BED_POSITION, 0, 0, 0));
            this.setDataFlag(DATA_PLAYER_FLAGS, DATA_PLAYER_FLAG_SLEEP, false);

            this.level.sleepTicks = 0;

            AnimatePacket pk = new AnimatePacket();
            pk.eid = 0;
            pk.action = 3; // Wake up
            this.dataPacket(pk);
        }
    }

    public boolean setGamemode(int gamemode) {
        if (gamemode < 0 || gamemode > 3 || this.gamemode == gamemode) {
            return false;
        }

        AdventureSettings newSettings = this.getAdventureSettings().clone(this);
        newSettings.setCanDestroyBlock((gamemode & 0x02) == 0);
        newSettings.setCanFly((gamemode & 0x01) > 0);
        newSettings.setNoclip(gamemode == 0x03);

        PlayerGameModeChangeEvent ev;
        this.server.getPluginManager().callEvent(
                ev = new PlayerGameModeChangeEvent(this, gamemode, newSettings));

        if (ev.isCancelled()) {
            return false;
        }

        this.gamemode = gamemode;

        if (this.isSpectator()) {
            this.keepMovement = true;
            this.despawnFromAll();
        } else {
            this.keepMovement = false;
            this.spawnToAll();
        }

        this.namedTag.putInt("playerGameType", this.gamemode);

        SetPlayerGameTypePacket pk = new SetPlayerGameTypePacket();
        pk.gamemode = this.gamemode & 0x01;
        this.dataPacket(pk);

        this.setAdventureSettings(ev.getNewAdventureSettings());

        if (this.gamemode == ServerPlayer.SPECTATOR) {
            ContainerSetContentPacket containerSetContentPacket = new ContainerSetContentPacket();
            containerSetContentPacket.windowid = ContainerSetContentPacket.SPECIAL_CREATIVE;
            this.dataPacket(containerSetContentPacket);
        } else {
            ContainerSetContentPacket containerSetContentPacket = new ContainerSetContentPacket();
            containerSetContentPacket.windowid = ContainerSetContentPacket.SPECIAL_CREATIVE;
            containerSetContentPacket.slots = Item.getCreativeItems().toArray(Item[]::new);
            this.dataPacket(containerSetContentPacket);
        }

        this.inventory.sendContents(this);
        this.inventory.sendContents(this.getViewers().values());
        this.inventory.sendHeldItem(this.hasSpawned.values());

        return true;
    }

    @Deprecated
    public void sendSettings() {
        this.getAdventureSettings().update();
    }

    public boolean isSurvival() {
        return (this.gamemode & 0x01) == 0;
    }

    public boolean isCreative() {
        return (this.gamemode & 0x01) > 0;
    }

    public boolean isSpectator() {
        return this.gamemode == 3;
    }

    public boolean isAdventure() {
        return (this.gamemode & 0x02) > 0;
    }

    @Override
    public Item[] getDrops() {
        if (!this.isCreative()) {
            return super.getDrops();
        }

        return new Item[0];
    }

    @Override
    public boolean setDataProperty(EntityData data) {
        return setDataProperty(data, true);
    }

    @Override
    public boolean setDataProperty(EntityData data, boolean send) {
        if (super.setDataProperty(data, send)) {
            if (send)
                this.sendData(this, new EntityMetadata().put(this.getDataProperty(data.getId())));
            return true;
        }
        return false;
    }

    @Override
    protected void checkGroundState(double movX, double movY, double movZ, double dx, double dy,
                                    double dz) {
        if (!this.onGround || movX != 0 || movY != 0 || movZ != 0) {
            boolean onGround = false;

            AxisAlignedBB bb = this.boundingBox.clone();
            bb.maxY = bb.minY + 0.5;
            bb.minY -= 1;

            AxisAlignedBB realBB = this.boundingBox.clone();
            realBB.maxY = realBB.minY + 0.1;
            realBB.minY -= 0.2;

            int minX = MathUtilities.floorDouble(bb.minX);
            int minY = MathUtilities.floorDouble(bb.minY);
            int minZ = MathUtilities.floorDouble(bb.minZ);
            int maxX = MathUtilities.ceilDouble(bb.maxX);
            int maxY = MathUtilities.ceilDouble(bb.maxY);
            int maxZ = MathUtilities.ceilDouble(bb.maxZ);

            for (int z = minZ; z <= maxZ; ++z) {
                for (int x = minX; x <= maxX; ++x) {
                    for (int y = minY; y <= maxY; ++y) {
                        Block block = this.level.getBlock(this.getTemporalVector().setComponents(x, y, z));

                        if (!block.canPassThrough() && block.collidesWithBB(realBB)) {
                            onGround = true;
                            break;
                        }
                    }
                }
            }

            this.onGround = onGround;
        }

        this.isCollided = this.onGround;
    }

    @Override
    protected void checkBlockCollision() {
        for (Block block : this.getBlocksAround()) {
            block.onEntityCollide(this);
        }
    }

    protected void checkNearEntities() {
        for (Entity entity : this.level.getNearbyEntities(this.boundingBox.grow(1, 0.5, 1), this)) {
            entity.scheduleUpdate();

            if (!entity.isAlive() || !this.isAlive()) {
                continue;
            }

            if (entity instanceof EntityArrow && ((EntityArrow) entity).hadCollision) {
                ItemArrow item = new ItemArrow();
                if (this.isSurvival() && !this.inventory.canAddItem(item)) {
                    continue;
                }

                InventoryPickupArrowEvent ev;
                this.server.getPluginManager().callEvent(
                        ev = new InventoryPickupArrowEvent(this.inventory, (EntityArrow) entity));
                if (ev.isCancelled()) {
                    continue;
                }

                TakeItemEntityPacket pk = new TakeItemEntityPacket();
                pk.entityId = this.getId();
                pk.target = entity.getId();
                Server.broadcastPacket(entity.getViewers().values(), pk);

                pk = new TakeItemEntityPacket();
                pk.entityId = 0;
                pk.target = entity.getId();
                this.dataPacket(pk);

                this.inventory.addItem(item.clone());
                entity.kill();
            } else if (entity instanceof EntityItem) {
                if (((EntityItem) entity).getPickupDelay() <= 0) {
                    Item item = ((EntityItem) entity).getItem();

                    if (item != null) {
                        if (this.isSurvival() && !this.inventory.canAddItem(item)) {
                            continue;
                        }

                        InventoryPickupItemEvent ev;
                        this.server.getPluginManager().callEvent(
                                ev = new InventoryPickupItemEvent(this.inventory, (EntityItem) entity));
                        if (ev.isCancelled()) {
                            continue;
                        }

                        // todo: achievement
                        /*
                         * switch (item.getId()) {
                         * case Item.WOOD:
                         * this.awardAchievement("mineWood");
                         * break;
                         * case Item.DIAMOND:
                         * this.awardAchievement("diamond");
                         * break;
                         * }
                         */

                        TakeItemEntityPacket pk = new TakeItemEntityPacket();
                        pk.entityId = this.getId();
                        pk.target = entity.getId();
                        Server.broadcastPacket(entity.getViewers().values(), pk);

                        pk = new TakeItemEntityPacket();
                        pk.entityId = 0;
                        pk.target = entity.getId();
                        this.dataPacket(pk);

                        this.inventory.addItem(item.clone());
                        entity.kill();
                    }
                }
            }
        }
        for (Entity entity : this.level.getNearbyEntities(this.boundingBox.grow(3.5, 2, 3.5), this)) {
            entity.scheduleUpdate();

            if (!entity.isAlive() || !this.isAlive()) {
                continue;
            }
            if (entity instanceof EntityXPOrb xpOrb) {
                if (xpOrb.getPickupDelay() <= 0) {
                    int exp = xpOrb.getExp();
                    this.addExperience(exp);
                    entity.kill();
                    this.getLevel().addSound(new ExperienceOrbSound(this));
                    break;
                }
            }
        }
    }

    protected void processMovement(int tickDiff) {
        if (!this.isAlive() || !this.isSpawned() || this.newPosition == null ||  this.getTeleportPosition() != null) {
            return;
        }

        Location from = new Location(this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch, this.level);

        Vector3 newPos = this.newPosition;
        double distanceSquared = newPos.distanceSquared(this);
        boolean revert = false;

        if ((distanceSquared / ((double) (tickDiff * tickDiff))) > 100 && (newPos.y - this.y) > -5) {
            revertMovement(from);
            return;
        }

        if (this.chunk == null || !this.chunk.isGenerated()) {
            var chunk = this.level.getChunk((int) newPos.x >> 4, (int) newPos.z >> 4, false);

            if (chunk == null || !chunk.isGenerated()) {
                revertMovement(from);
                this.nextChunkOrderRun = 0;

                return;
            }

            if (this.chunk != null) {
                this.chunk.removeEntity(this);
            }

            this.chunk = chunk;
        }

        double tdx = newPos.x - this.x;
        double tdz = newPos.z - this.z;
        double distance = Math.sqrt(tdx * tdx + tdz * tdz);

        if (distanceSquared != 0) {
            double dx = newPos.x - this.x;
            double dy = newPos.y - this.y;
            double dz = newPos.z - this.z;

            this.fastMove(dx, dy, dz);

            double diffX = this.x - newPos.x;
            double diffY = this.y - newPos.y;
            double diffZ = this.z - newPos.z;

            double yS = 0.5 + this.ySize;
            if (diffY >= -yS || diffY <= yS) {
                diffY = 0;
            }

            double diff =
                    (diffX * diffX + diffY * diffY + diffZ * diffZ) / ((double) (tickDiff * tickDiff));

            if (!server.getAllowFlight() && this.isSurvival()) {
                if (!this.isSleeping()) {
                    if (diff > 0.225) {
                        revertMovement(from);

                        this.server.getLogger().warning(this.getServer().getLanguage().translateString(
                                "crimsonmc.player.invalidMove", this.getName()));
                        return;
                    }
                }
            }

            if (diff > 0) {
                this.x = newPos.x;
                this.y = newPos.y;
                this.z = newPos.z;
                double radius = this.getWidth() / 2;
                this.boundingBox.setBounds(this.x - radius, this.y, this.z - radius, this.x + radius,
                        this.y + this.getHeight(), this.z + radius);
            }
        }

        Location to = this.getLocation();

        if (!revert && (Math.pow(this.lastX - to.x, 2) + Math.pow(this.lastY - to.y, 2) +
                Math.pow(this.lastZ - to.z, 2)) > ((double) 1 / 16) ||
                (Math.abs(this.lastYaw - to.yaw) + Math.abs(this.lastPitch - to.pitch)) > 10) {
            boolean isFirst = this.firstMove;

            this.firstMove = false;
            this.lastX = to.x;
            this.lastY = to.y;
            this.lastZ = to.z;

            this.lastYaw = to.yaw;
            this.lastPitch = to.pitch;

            if (!isFirst) {
                PlayerMoveEvent ev = new PlayerMoveEvent(this, from, to);

                this.server.getPluginManager().callEvent(ev);

                if (!(revert = ev.isCancelled())) { // Yes, this is intended
                    if (!to.equals(ev.getTo())) {     // If plugins modify the destination
                        this.teleport(ev.getTo(), null);
                    } else {
                        this.addMovement(this.x, this.y + this.getEyeHeight(), this.z, this.yaw, this.pitch,
                                this.yaw);
                    }
                }
            }

            if (!this.isSpectator()) {
                this.checkNearEntities();
            }
            if (this.speed == null)
                speed = new Vector3(from.x - to.x, from.y - to.y, from.z - to.z);
            else
                this.speed.setComponents(from.x - to.x, from.y - to.y, from.z - to.z);
        } else {
            if (this.speed == null)
                speed = new Vector3(0, 0, 0);
            else
                this.speed.setComponents(0, 0, 0);
        }

        if (!revert && (this.isFoodEnabled() || this.getServer().getDifficulty() == 0)) {
            if ((this.isSurvival() ||
                    this.isAdventure()) /* && !this.getRiddingOn() instanceof Entity */) {

                // UpdateFoodExpLevel
                if (distance >= 0.05) {
                    double jump = 0;
                    double swimming = this.isInsideOfWater() ? 0.015 * distance : 0;
                    if (swimming != 0)
                        distance = 0;
                    if (this.isSprinting()) { // Running
                        if (this.inAirTicks == 3 && swimming == 0) {
                            jump = 0.7;
                        }
                        this.getFoodData().updateFoodExpLevel(0.1 * distance + jump + swimming);
                    } else {
                        if (this.inAirTicks == 3 && swimming == 0) {
                            jump = 0.2;
                        }
                        this.getFoodData().updateFoodExpLevel(0.01 * distance + jump + swimming);
                    }
                }
            }
        }

        if (revert) {

        } else {
            this.forceMovement = null;
            if (distanceSquared != 0 && this.nextChunkOrderRun > 20) {
                this.nextChunkOrderRun = 20;
            }
        }

        this.newPosition = null;
    }

    private void revertMovement(Location from) {
        this.lastX = from.x;
        this.lastY = from.y;
        this.lastZ = from.z;

        this.lastYaw = from.yaw;
        this.lastPitch = from.pitch;

        this.sendPosition(from, from.yaw, from.pitch, MovePlayerPacket.MODE_RESET);
        // this.sendSettings();
        this.forceMovement = new Vector3(from.x, from.y, from.z);
    }

    @Override
    public boolean setMotion(Vector3 motion) {
        if (super.setMotion(motion)) {
            if (this.chunk != null) {
                this.level.addEntityMotion(this.chunk.getX(), this.chunk.getZ(), this.getId(), this.motionX,
                        this.motionY, this.motionZ);
                SetEntityMotionPacket pk = new SetEntityMotionPacket();
                pk.entities = new SetEntityMotionPacket.Entry[]{
                        new SetEntityMotionPacket.Entry(0, (float) motion.x, (float) motion.y, (float) motion.z)};
                this.dataPacket(pk);
            }

            if (this.motionY > 0) {
                // todo: check this
                this.startAirTicks =
                        (int) ((-(Math.log(this.getGravity() /
                                (this.getGravity() + this.getDrag() * this.motionY))) /
                                this.getDrag()) *
                                2 +
                                5);
            }

            return true;
        }

        return false;
    }

    @Override
    public void addMovement(double x, double y, double z, double yaw, double pitch, double headYaw) {
        this.level.addPlayerMovement(this.chunk.getX(), this.chunk.getZ(), this.id, x, y, z, yaw, pitch,
                this.isOnGround());
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (!this.loggedIn) {
            return false;
        }

        int tickDiff = currentTick - this.lastUpdate;

        if (tickDiff <= 0) {
            return true;
        }

        this.messageCounter = 2;

        this.lastUpdate = currentTick;

        if (!this.isAlive() && this.isSpawned()) {
            ++this.deadTicks;
            if (this.deadTicks >= 10) {
                this.despawnFromAll();
            }
            return true;
        }

        if (this.isSpawned()) {
            this.processMovement(tickDiff);

            this.entityBaseTick(tickDiff);

            if (this.isOnFire() && this.lastUpdate % 10 == 0) {
                if (this.isCreative() && !this.isInsideOfFire()) {
                    this.extinguish();
                } else if (this.getLevel().isRaining()) {
                    if (this.getLevel().canBlockSeeSky(this)) {
                        this.extinguish();
                    }
                }
            }

            if (!this.isSpectator() && this.speed != null) {
                if (this.onGround) {
                    if (this.inAirTicks != 0) {
                        this.startAirTicks = 5;
                    }
                    this.inAirTicks = 0;
                    this.highestPosition = this.y;
                } else {
                    if (!this.getAdventureSettings().canFly() && this.inAirTicks > 10 && !this.isSleeping() &&
                            !this.getDataPropertyBoolean(DATA_NO_AI)) {
                        double expectedVelocity =
                                (-this.getGravity()) / ((double) this.getDrag()) -
                                        ((-this.getGravity()) / ((double) this.getDrag())) *
                                                Math.exp(-((double) this.getDrag()) *
                                                        ((double) (this.inAirTicks - this.startAirTicks)));
                        double diff = (this.speed.y - expectedVelocity) * (this.speed.y - expectedVelocity);

                        if (!this.hasEffect(Effect.JUMP) && diff > 0.6 && expectedVelocity < this.speed.y) {
                            if (this.inAirTicks < 100) {
                                // this.sendSettings();
                                this.setMotion(new Vector3(0, expectedVelocity, 0));
                            } else if (this.kick("Flying is not enabled on this server")) {
                                return false;
                            }
                        }
                    }

                    if (this.y > highestPosition) {
                        this.highestPosition = this.y;
                    }

                    ++this.inAirTicks;
                }

                if (this.isSurvival() || this.isAdventure()) {
                    if (this.getFoodData() != null)
                        this.getFoodData().update(tickDiff);
                }
            }
        }

        this.checkTeleportPosition();

        return true;
    }

    public void checkNetwork() {
        if (!this.isOnline()) {
            return;
        }

        if (this.nextChunkOrderRun-- <= 0 || this.chunk == null) {
            this.orderChunks();
        }

        if (!this.loadQueue.isEmpty() || !this.isSpawned()) {
            this.sendNextChunk();
        }

        if (!this.batchedPackets.isEmpty()) {
            for (int channel : this.batchedPackets.keySet()) {
                this.server.batchPackets(new ServerPlayer[]{this},
                        batchedPackets.get(channel).toArray(DataPacket[]::new), false);
            }
            this.batchedPackets = new TreeMap<>();
        }
    }

    public boolean canInteract(Vector3 pos, double maxDistance) {
        return this.canInteract(pos, maxDistance, 0.5);
    }

    public boolean canInteract(Vector3 pos, double maxDistance, double maxDiff) {
        if (this.distanceSquared(pos) > maxDistance * maxDistance) {
            return false;
        }

        Vector2 dV = this.getDirectionPlane();
        double dot = dV.dot(new Vector2(this.x, this.z));
        double dot1 = dV.dot(new Vector2(pos.x, pos.z));
        return (dot1 - dot) >= -maxDiff;
    }

    public void onPlayerPreLogin() {
        // TODO: AUTHENTICATE
        this.tryAuthenticate();
    }

    public void tryAuthenticate() {
        PlayStatusPacket pk = new PlayStatusPacket();
        pk.status = PlayStatusPacket.LOGIN_SUCCESS;

        this.dataPacket(pk);
        this.authenticateCallback(true);
    }

    public void authenticateCallback(boolean valid) {
        // TODO add more stuff after authentication is available

        if (!valid) {
            this.close("", "disconnectionScreen.invalidSession");
            return;
        }

        this.processLogin();
    }

    protected void processLogin() {
        if (!this.server.isWhitelisted((this.getName()).toLowerCase())) {
            this.close(this.getLeaveMessage(), "Server is white-listed");

            return;
        } else if (this.server.getNameBans().isBanned(this.getName().toLowerCase()) ||
                this.server.getIPBans().isBanned(this.getAddress())) {
            this.close(this.getLeaveMessage(), "You are banned");

            return;
        }

        if (this.hasPermission(Server.BROADCAST_CHANNEL_USERS)) {
            this.server.getPluginManager().subscribeToPermission(Server.BROADCAST_CHANNEL_USERS, this);
        }
        if (this.hasPermission(Server.BROADCAST_CHANNEL_ADMINISTRATIVE)) {
            this.server.getPluginManager().subscribeToPermission(Server.BROADCAST_CHANNEL_ADMINISTRATIVE,
                    this);
        }

        for (ServerPlayer p : new ArrayList<>(this.server.getOnlinePlayers().values())) {
            if (p != this && p.getName() != null && this.getName() != null &&
                    Objects.equals(p.getName().toLowerCase(), this.getName().toLowerCase())) {
                if (!p.kick("logged in from another location")) {
                    this.close(this.getLeaveMessage(), "Logged in from another location");

                    return;
                }
            } else if (p.loggedIn && this.getUniqueId().equals(p.getUniqueId())) {
                if (!p.kick("logged in from another location")) {
                    this.close(this.getLeaveMessage(), "Logged in from another location");

                    return;
                }
            }
        }

        CompoundTag nbt = this.server.getOfflinePlayerData(this.getUsername());
        if (nbt == null) {
            this.close(this.getLeaveMessage(), "Invalid data");

            return;
        }

        this.playedBefore = (nbt.getLong("lastPlayed") - nbt.getLong("firstPlayed")) > 1;

        boolean alive = true;

        nbt.putString("NameTag", this.getUsername());

        if (0 >= nbt.getShort("Health")) {
            alive = false;
        }

        int exp = nbt.getInt("EXP");
        int expLevel = nbt.getInt("expLevel");
        this.setExperience(exp, expLevel);

        this.gamemode = nbt.getInt("playerGameType") & 0x03;
        if (this.server.getForceGamemode()) {
            this.gamemode = this.server.getGamemode();
            nbt.putInt("playerGameType", this.gamemode);
        }

        this.adventureSettings = new AdventureSettings.Builder(this)
                .canDestroyBlock(!isAdventure())
                .autoJump(true)
                .canFly(isCreative())
                .noclip(isSpectator())
                .build();

        Level level;
        if ((level = this.server.getLevelByName(nbt.getString("Level"))) == null || !alive) {
            this.setLevel(this.server.getDefaultLevel());
            nbt.putString("Level", this.level.getName());
            nbt.getList("Pos", DoubleTag.class)
                    .add(new DoubleTag("0", this.level.getSpawnLocation().x))
                    .add(new DoubleTag("1", this.level.getSpawnLocation().y))
                    .add(new DoubleTag("2", this.level.getSpawnLocation().z));
        } else {
            this.setLevel(level);
        }

        // todo achievement
        nbt.putLong("lastPlayed", System.currentTimeMillis() / 1000);

        if (this.server.getAutoSave()) {
            this.server.saveOfflinePlayerData(this.getUsername(), nbt, true);
        }

        ListTag<DoubleTag> posList = nbt.getList("Pos", DoubleTag.class);

        super.init(
                this.level.getChunk((int) posList.get(0).data >> 4, (int) posList.get(2).data >> 4, true),
                nbt);

        if (!this.namedTag.contains("foodLevel")) {
            this.namedTag.putInt("foodLevel", 20);
        }
        int foodLevel = this.namedTag.getInt("foodLevel");
        if (!this.namedTag.contains("FoodSaturationLevel")) {
            this.namedTag.putFloat("FoodSaturationLevel", 20);
        }
        float foodSaturationLevel = this.namedTag.getFloat("foodSaturationLevel");
        this.foodData = new PlayerFood(this, foodLevel, foodSaturationLevel);

        PlayerLoginEvent ev;
        this.server.getPluginManager().callEvent(ev = new PlayerLoginEvent(this, "Plugin reason"));
        if (ev.isCancelled()) {
            this.close(this.getLeaveMessage(), ev.getKickMessage());

            return;
        }

        this.server.addOnlinePlayer(this, false);
        this.loggedIn = true;

        if (this.isCreative()) {
            this.inventory.setHeldItemSlot(0);
        } else {
            this.inventory.setHeldItemSlot(this.inventory.getHotbarSlotIndex(0));
        }

        if (this.isSpectator())
            this.keepMovement = true;

        PlayStatusPacket statusPacket = new PlayStatusPacket();
        statusPacket.status = PlayStatusPacket.LOGIN_SUCCESS;
        this.dataPacket(statusPacket);

        if (this.spawnPosition == null && this.namedTag.contains("SpawnLevel") &&
                (level = this.server.getLevelByName(this.namedTag.getString("SpawnLevel"))) != null) {
            this.spawnPosition =
                    new Position(this.namedTag.getInt("SpawnX"), this.namedTag.getInt("SpawnY"),
                            this.namedTag.getInt("SpawnZ"), level);
        }

        Position spawnPosition = this.getSpawn();

        StartGamePacket startGamePacket = new StartGamePacket();
        startGamePacket.seed = -1;
        startGamePacket.dimension = (byte) (getLevel().getDimension() & 0xFF);
        startGamePacket.x = (float) this.x;
        startGamePacket.y = (float) this.y;
        startGamePacket.z = (float) this.z;
        startGamePacket.spawnX = (int) spawnPosition.x;
        startGamePacket.spawnY = (int) spawnPosition.y;
        startGamePacket.spawnZ = (int) spawnPosition.z;
        startGamePacket.generator = 1; // 0 old, 1 infinite, 2 flat
        startGamePacket.gamemode = this.gamemode & 0x01;
        startGamePacket.eid = 0; // Always use EntityID as zero for the actual player
        startGamePacket.b1 = true;
        startGamePacket.b2 = true;
        startGamePacket.b3 = false;
        startGamePacket.unknownstr = "";
        this.dataPacket(startGamePacket);

        SetTimePacket setTimePacket = new SetTimePacket();
        setTimePacket.time = this.level.getTime();
        setTimePacket.started = !this.level.stopTime;
        this.dataPacket(setTimePacket);

        SetSpawnPositionPacket setSpawnPositionPacket = new SetSpawnPositionPacket();
        setSpawnPositionPacket.x = (int) spawnPosition.x;
        setSpawnPositionPacket.y = (int) spawnPosition.y;
        setSpawnPositionPacket.z = (int) spawnPosition.z;
        this.dataPacket(setSpawnPositionPacket);

        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.entityId = 0;
        updateAttributesPacket.entries = new Attribute[]{
                Attribute.getAttribute(Attribute.MAX_HEALTH)
                        .setMaxValue(this.getMaxHealth())
                        .setValue(this.getHealth()),
                Attribute.getAttribute(Attribute.MOVEMENT_SPEED).setValue(this.getMovementSpeed())};
        this.dataPacket(updateAttributesPacket);

        SetDifficultyPacket setDifficultyPacket = new SetDifficultyPacket();
        setDifficultyPacket.difficulty = this.server.getDifficulty();
        this.dataPacket(setDifficultyPacket);

        this.server.getLogger().info(this.getServer().getLanguage().translateString(
                "crimsonmc.player.logIn", TextFormat.AQUA + this.getUsername() + TextFormat.WHITE, this.ip,
                String.valueOf(this.port), String.valueOf(this.id), this.level.getName(),
                String.valueOf(MathUtilities.round(this.x, 4)), String.valueOf(MathUtilities.round(this.y, 4)),
                String.valueOf(MathUtilities.round(this.z, 4))));

        if (this.isOp()) {
            this.setRemoveFormat(false);
        }

        if (this.gamemode == ServerPlayer.SPECTATOR) {
            ContainerSetContentPacket containerSetContentPacket = new ContainerSetContentPacket();
            containerSetContentPacket.windowid = ContainerSetContentPacket.SPECIAL_CREATIVE;
            this.dataPacket(containerSetContentPacket);
        } else {
            ContainerSetContentPacket containerSetContentPacket = new ContainerSetContentPacket();
            containerSetContentPacket.windowid = ContainerSetContentPacket.SPECIAL_CREATIVE;
            containerSetContentPacket.slots = Item.getCreativeItems().toArray(Item[]::new);
            this.dataPacket(containerSetContentPacket);
        }

        this.forceMovement = this.teleportPosition = this.getPosition();

        this.server.onPlayerLogin(this);
    }

    public void handleDataPacket(DataPacket packet) {
        PacketHandler<?> handler;

        if (!connected) {
            return;
        }

        DataPacketReceiveEvent ev = new DataPacketReceiveEvent(this, packet);
        this.server.getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            return;
        }

        if (packet.pid() == ProtocolInfo.BATCH_PACKET) {
            this.server.getNetwork().processBatch((BatchPacket) packet, this);
            return;
        }

        if ((handler = PlayerPacketHandler.getPacketFromPid(packet.pid())) != null) {
            try {
                handler.getClass()
                        .getMethod("handlePacket", ServerPlayer.class, DataPacket.class)
                        .invoke(handler, this, packet);

            } catch (Exception exception) {
                this.getServer().getLogger().error("Could not invoke PacketHandler.handlePacket: ",
                        exception);
            }

            return;
        }

        switch (packet.pid()) {
            case ProtocolInfo.USE_ITEM_PACKET:
                break;
            case ProtocolInfo.PLAYER_ACTION_PACKET:
                if (!this.isSpawned() || this.blocked ||
                        (!this.isAlive() &&
                                ((PlayerActionPacket) packet).action != PlayerActionPacket.ACTION_RESPAWN &&
                                ((PlayerActionPacket) packet).action != PlayerActionPacket.ACTION_DIMENSION_CHANGE)) {
                    break;
                }

                ((PlayerActionPacket) packet).entityId = this.id;
                Vector3 pos = new Vector3(((PlayerActionPacket) packet).x, ((PlayerActionPacket) packet).y,
                        ((PlayerActionPacket) packet).z);

                switch (((PlayerActionPacket) packet).action) {
                    case PlayerActionPacket.ACTION_START_BREAK:
                        if (this.lastBreak != Long.MAX_VALUE || pos.distanceSquared(this) > 10000) {
                            break;
                        }
                        Block target = this.level.getBlock(pos);
                        PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(
                                this, this.inventory.getItemInHand(), target, ((PlayerActionPacket) packet).face,
                                target.getId() == 0 ? PlayerInteractEvent.LEFT_CLICK_AIR
                                        : PlayerInteractEvent.LEFT_CLICK_BLOCK);
                        this.getServer().getPluginManager().callEvent(playerInteractEvent);
                        if (playerInteractEvent.isCancelled()) {
                            this.inventory.sendHeldItem(this);
                            break;
                        }
                        Block block = target.getSide(((PlayerActionPacket) packet).face);
                        if (block.getId() == Block.FIRE) {
                            this.level.setBlock(block, new BlockAir(), true);
                        }
                        this.lastBreak = System.currentTimeMillis();
                        break;

                    case PlayerActionPacket.ACTION_ABORT_BREAK:
                        this.lastBreak = Long.MAX_VALUE;
                        break;

                    case PlayerActionPacket.ACTION_RELEASE_ITEM:
                        if (this.getStartAction() > -1 &&
                                this.getDataFlag(ServerPlayer.DATA_FLAGS, ServerPlayer.DATA_FLAG_ACTION)) {
                            if (this.inventory.getItemInHand().getId() == Item.BOW) {

                                Item bow = this.inventory.getItemInHand();
                                ItemArrow itemArrow = new ItemArrow();
                                if (this.isSurvival() && !this.inventory.contains(itemArrow)) {
                                    this.inventory.sendContents(this);
                                    break;
                                }

                                double damage = 2;
                                boolean flame = false;

                                if (bow.hasEnchantments()) {
                                    Enchantment bowDamage = bow.getEnchantment(Enchantment.ID_BOW_POWER);

                                    if (bowDamage != null && bowDamage.getLevel() > 0) {
                                        damage += 0.25 * (bowDamage.getLevel() + 1);
                                    }

                                    Enchantment flameEnchant = bow.getEnchantment(Enchantment.ID_BOW_FLAME);
                                    flame = flameEnchant != null && flameEnchant.getLevel() > 0;
                                }

                                CompoundTag nbt =
                                        new CompoundTag()
                                                .putList(new ListTag<DoubleTag>("Pos")
                                                        .add(new DoubleTag("", x))
                                                        .add(new DoubleTag("", y + this.getEyeHeight()))
                                                        .add(new DoubleTag("", z)))
                                                .putList(new ListTag<DoubleTag>("Motion")
                                                        .add(new DoubleTag("", -Math.sin(yaw / 180 * Math.PI) *
                                                                Math.cos(pitch / 180 * Math.PI)))
                                                        .add(new DoubleTag("", -Math.sin(pitch / 180 * Math.PI)))
                                                        .add(new DoubleTag("", Math.cos(yaw / 180 * Math.PI) *
                                                                Math.cos(pitch / 180 * Math.PI))))
                                                .putList(new ListTag<FloatTag>("Rotation")
                                                        .add(new FloatTag("", (float) yaw))
                                                        .add(new FloatTag("", (float) pitch)))
                                                .putShort("Fire", this.isOnFire() || flame ? 45 * 60 : 0)
                                                .putDouble("damage", damage);

                                EntityShootBowEvent entityShootBowEvent = getEntityShootBowEvent(bow, nbt);

                                this.server.getPluginManager().callEvent(entityShootBowEvent);
                                if (entityShootBowEvent.isCancelled()) {
                                    entityShootBowEvent.getProjectile().kill();
                                    this.inventory.sendContents(this);
                                } else {
                                    entityShootBowEvent.getProjectile().setMotion(
                                            entityShootBowEvent.getProjectile().getMotion().multiply(
                                                    entityShootBowEvent.getForce()));
                                    if (this.isSurvival()) {
                                        Enchantment infinity;

                                        if (!bow.hasEnchantments() ||
                                                (infinity = bow.getEnchantment(Enchantment.ID_BOW_INFINITY)) == null ||
                                                infinity.getLevel() <= 0)
                                            this.inventory.removeItem(itemArrow);

                                        if (!bow.isUnbreakable()) {
                                            Enchantment durability = bow.getEnchantment(Enchantment.ID_DURABILITY);
                                            if (!(durability != null && durability.getLevel() > 0 &&
                                                    (100 / (durability.getLevel() + 1)) <= new Random().nextInt(100))) {
                                                bow.setDamage(bow.getDamage() + 1);
                                                if (bow.getDamage() >= 385) {
                                                    this.inventory.setItemInHand(new ItemBlock(new BlockAir(), 0, 0));
                                                } else {
                                                    this.inventory.setItemInHand(bow);
                                                }
                                            }
                                        }
                                    }
                                    if (entityShootBowEvent.getProjectile() != null) {
                                        ProjectileLaunchEvent projectev =
                                                new ProjectileLaunchEvent(entityShootBowEvent.getProjectile());
                                        this.server.getPluginManager().callEvent(projectev);
                                        if (projectev.isCancelled()) {
                                            entityShootBowEvent.getProjectile().kill();
                                        } else {
                                            entityShootBowEvent.getProjectile().spawnToAll();
                                            this.level.addSound(new LaunchSound(this), this.getViewers().values());
                                        }
                                    } else {
                                        entityShootBowEvent.getProjectile().spawnToAll();
                                    }
                                }
                            }
                        }
                        // milk removed here, see the section of food

                    case PlayerActionPacket.ACTION_STOP_SLEEPING:
                        this.stopSleep();
                        break;

                    case PlayerActionPacket.ACTION_RESPAWN:
                        if (!this.isSpawned() || this.isAlive() || !this.isOnline()) {
                            break;
                        }

                        if (this.server.isHardcore()) {
                            this.setBanned(true);
                            break;
                        }

                        this.craftingType = CRAFTING_SMALL;

                        PlayerRespawnEvent playerRespawnEvent = new PlayerRespawnEvent(this, this.getSpawn());
                        this.server.getPluginManager().callEvent(playerRespawnEvent);

                        this.teleport(playerRespawnEvent.getRespawnPosition(), null);

                        this.setSprinting(false);
                        this.setSneaking(false);

                        this.extinguish();
                        this.setDataProperty(new ShortEntityData(ServerPlayer.DATA_AIR, 300), false);
                        this.deadTicks = 0;
                        this.noDamageTicks = 60;

                        this.setHealth(this.getMaxHealth());
                        this.getFoodData().setLevel(20, 20);

                        this.removeAllEffects();
                        this.sendData(this);

                        this.setMovementSpeed(0.1f);

                        this.getAdventureSettings().update();
                        this.inventory.sendContents(this);
                        this.inventory.sendArmorContents(this);

                        this.blocked = false;

                        this.spawnToAll();
                        this.scheduleUpdate();
                        break;

                    case PlayerActionPacket.ACTION_START_SPRINT:
                        PlayerToggleSprintEvent playerToggleSprintEvent = new PlayerToggleSprintEvent(this, true);
                        this.server.getPluginManager().callEvent(playerToggleSprintEvent);
                        if (playerToggleSprintEvent.isCancelled()) {
                            this.sendData(this);
                        } else {
                            this.setSprinting(true);
                        }
                        break;

                    case PlayerActionPacket.ACTION_STOP_SPRINT:
                        playerToggleSprintEvent = new PlayerToggleSprintEvent(this, false);
                        this.server.getPluginManager().callEvent(playerToggleSprintEvent);
                        if (playerToggleSprintEvent.isCancelled()) {
                            this.sendData(this);
                        } else {
                            this.setSprinting(false);
                        }
                        break;

                    case PlayerActionPacket.ACTION_START_SNEAK:
                        PlayerToggleSneakEvent playerToggleSneakEvent = new PlayerToggleSneakEvent(this, true);
                        this.server.getPluginManager().callEvent(playerToggleSneakEvent);
                        if (playerToggleSneakEvent.isCancelled()) {
                            this.sendData(this);
                        } else {
                            this.setSneaking(true);
                        }
                        break;

                    case PlayerActionPacket.ACTION_STOP_SNEAK:
                        playerToggleSneakEvent = new PlayerToggleSneakEvent(this, false);
                        this.server.getPluginManager().callEvent(playerToggleSneakEvent);
                        if (playerToggleSneakEvent.isCancelled()) {
                            this.sendData(this);
                        } else {
                            this.setSneaking(false);
                        }
                        break;
                }

                this.setStartAction(-1);
                this.setDataFlag(ServerPlayer.DATA_FLAGS, ServerPlayer.DATA_FLAG_ACTION, false);
                break;

            case ProtocolInfo.REMOVE_BLOCK_PACKET:
                if (!this.isSpawned() || this.blocked || !this.isAlive()) {
                    break;
                }
                this.craftingType = CRAFTING_SMALL;

                Vector3 vector = new Vector3(((RemoveBlockPacket) packet).x, ((RemoveBlockPacket) packet).y,
                        ((RemoveBlockPacket) packet).z);

                Item item;
                if (this.isCreative()) {
                    item = this.inventory.getItemInHand();
                } else {
                    item = this.inventory.getItemInHand();
                }

                Item oldItem = item.clone();

                if (this.canInteract(vector.add(0.5, 0.5, 0.5), this.isCreative() ? 13 : 6) &&
                        (item = this.level.useBreakOn(vector, item, this, true)) != null) {
                    if (this.isSurvival()) {
                        this.getFoodData().updateFoodExpLevel(0.025);
                        if (!item.deepEquals(oldItem) || item.getCount() != oldItem.getCount()) {
                            this.inventory.setItemInHand(item);
                            this.inventory.sendHeldItem(this.hasSpawned.values());
                        }
                    }
                    break;
                }

                this.inventory.sendContents(this);
                Block target = this.level.getBlock(vector);
                BlockEntity blockEntity = this.level.getBlockEntity(vector);

                this.level.sendBlocks(new ServerPlayer[]{this}, new Block[]{target},
                        UpdateBlockPacket.FLAG_ALL_PRIORITY);

                this.inventory.sendHeldItem(this);

                if (blockEntity instanceof BlockEntitySpawnable) {
                    ((BlockEntitySpawnable) blockEntity).spawnTo(this);
                }
                break;

            case ProtocolInfo.INTERACT_PACKET:
                if (!this.isSpawned() || !this.isAlive() || this.blocked) {
                    break;
                }
                this.craftingType = CRAFTING_SMALL;
                Entity targetEntity = this.level.getEntity(((InteractPacket) packet).target);
                boolean cancelled =
                        targetEntity instanceof ServerPlayer && !((boolean) this.server.getConfig("pvp", true));

                if (targetEntity != null && this.isAlive() && targetEntity.isAlive()) {
                    if (this.getGamemode() == ServerPlayer.VIEW) {
                        cancelled = true;
                    }

                    if (targetEntity instanceof EntityItem || targetEntity instanceof EntityArrow) {
                        this.kick("Attempting to attack an invalid entity");
                        this.server.getLogger().warning(this.getServer().getLanguage().translateString(
                                "crimsonmc.player.invalidEntity", this.getName()));
                        break;
                    }

                    item = this.inventory.getItemInHand();
                    float itemDamage = item.getAttackDamage();

                    for (Enchantment enchantment : item.getEnchantments()) {
                        itemDamage += (float) enchantment.getDamageBonus(targetEntity);
                    }

                    HashMap<Integer, Float> damage = new HashMap<>();
                    damage.put(EntityDamageEvent.MODIFIER_BASE, itemDamage);

                    if (!this.canInteract(targetEntity, 8)) {
                        cancelled = true;
                    } else if (targetEntity instanceof ServerPlayer) {
                        if ((((ServerPlayer) targetEntity).getGamemode() & 0x01) > 0) {
                            break;
                        } else if (!this.server.getPropertyBoolean("pvp") || this.server.getDifficulty() == 0) {
                            cancelled = true;
                        }
                    } else if (targetEntity instanceof EntityVehicle) {
                        SetEntityLinkPacket pk;
                        switch (((InteractPacket) packet).action) {
                            case InteractPacket.ACTION_RIGHT_CLICK:
                                cancelled = true;

                                if (((EntityVehicle) targetEntity).linkedEntity != null) {
                                    break;
                                }
                                pk = new SetEntityLinkPacket();
                                pk.rider = targetEntity.getId();
                                pk.riding = this.id;
                                pk.type = 2;
                                Server.broadcastPacket(this.hasSpawned.values(), pk);

                                pk = new SetEntityLinkPacket();
                                pk.rider = targetEntity.getId();
                                pk.riding = 0;
                                pk.type = 2;
                                dataPacket(pk);

                                riding = targetEntity;
                                ((EntityVehicle) targetEntity).linkedEntity = this;

                                this.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, true);
                                break;
                            case InteractPacket.ACTION_VEHICLE_EXIT:
                                pk = new SetEntityLinkPacket();
                                pk.rider = targetEntity.getId();
                                pk.riding = this.id;
                                pk.type = 3;
                                Server.broadcastPacket(this.hasSpawned.values(), pk);

                                pk = new SetEntityLinkPacket();
                                pk.rider = targetEntity.getId();
                                pk.riding = 0;
                                pk.type = 3;
                                dataPacket(pk);

                                cancelled = true;
                                riding = null;
                                ((EntityVehicle) targetEntity).linkedEntity = null;
                                this.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, false);
                                break;
                        }
                    }

                    EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent(
                            this, targetEntity, EntityDamageEvent.CAUSE_ENTITY_ATTACK, damage);
                    if (cancelled) {
                        entityDamageByEntityEvent.setCancelled();
                    }

                    targetEntity.attack(entityDamageByEntityEvent);

                    if (entityDamageByEntityEvent.isCancelled()) {
                        if (item.isTool() && this.isSurvival()) {
                            this.inventory.sendContents(this);
                        }
                        break;
                    }

                    if (item.isTool() && this.isSurvival()) {
                        if (item.useOn(targetEntity) && item.getDamage() >= item.getMaxDurability()) {
                            this.inventory.setItemInHand(new ItemBlock(new BlockAir()));
                        } else {
                            this.inventory.setItemInHand(item);
                        }
                    }
                }

                break;
            case ProtocolInfo.ANIMATE_PACKET:
                if (!this.isSpawned() || !this.isAlive()) {
                    break;
                }

                PlayerAnimationEvent animationEvent =
                        new PlayerAnimationEvent(this, ((AnimatePacket) packet).action);
                this.server.getPluginManager().callEvent(animationEvent);
                if (animationEvent.isCancelled()) {
                    break;
                }

                AnimatePacket animatePacket = new AnimatePacket();
                animatePacket.eid = this.getId();
                animatePacket.action = animationEvent.getAnimationType();
                Server.broadcastPacket(this.getViewers().values(), animatePacket);
                break;
            case ProtocolInfo.SET_HEALTH_PACKET:
                // use UpdateAttributePacket instead
                break;

            case ProtocolInfo.ENTITY_EVENT_PACKET:
                if (!this.isSpawned() || this.blocked || !this.isAlive()) {
                    break;
                }
                this.craftingType = CRAFTING_SMALL;

                this.setDataFlag(DATA_FLAGS, DATA_FLAG_ACTION, false); // TODO: check if this should be true
                EntityEventPacket entityEventPacket = (EntityEventPacket) packet;

                if (entityEventPacket.event == EntityEventPacket.USE_ITEM) { // Eating
                    Item itemInHand = this.inventory.getItemInHand();
                    PlayerItemConsumeEvent consumeEvent = new PlayerItemConsumeEvent(this, itemInHand);
                    this.server.getPluginManager().callEvent(consumeEvent);
                    if (consumeEvent.isCancelled()) {
                        this.inventory.sendContents(this);
                        break;
                    }

                    if (itemInHand.getId() == Item.POTION) {
                        Potion potion = Potion.getPotion(itemInHand.getDamage()).setSplash(false);

                        if (this.getGamemode() == SURVIVAL) {
                            if (itemInHand.getCount() > 1) {
                                ItemGlassBottle bottle = new ItemGlassBottle();
                                if (this.inventory.canAddItem(bottle)) {
                                    this.inventory.addItem(bottle);
                                }
                                --itemInHand.count;
                            } else {
                                itemInHand = new ItemGlassBottle();
                            }
                        }

                        if (potion != null) {
                            potion.applyPotion(this);
                        }

                    } else {
                        EntityEventPacket pk = new EntityEventPacket();
                        pk.eid = this.getId();
                        pk.event = EntityEventPacket.USE_ITEM;
                        this.dataPacket(pk);
                        Server.broadcastPacket(this.getViewers().values(), pk);

                        Food food = Food.getByRelative(itemInHand);
                        if (food != null)
                            if (food.eatenBy(this))
                                --itemInHand.count;
                    }

                    this.inventory.setItemInHand(itemInHand);
                    this.inventory.sendHeldItem(this);
                }
                break;
            case ProtocolInfo.DROP_ITEM_PACKET:
                if (!this.isSpawned() || this.blocked || !this.isAlive()) {
                    break;
                }
                DropItemPacket dropItem = (DropItemPacket) packet;

                if (dropItem.item.getId() <= 0) {
                    break;
                }

                item =
                        this.inventory.contains(dropItem.item) ? dropItem.item : this.inventory.getItemInHand();
                PlayerDropItemEvent dropItemEvent = new PlayerDropItemEvent(this, item);
                this.server.getPluginManager().callEvent(dropItemEvent);
                if (dropItemEvent.isCancelled()) {
                    this.inventory.sendContents(this);
                    break;
                }

                this.inventory.removeItem(item);
                Vector3 motion = this.getDirectionVector().multiply(0.4);

                this.level.dropItem(this.add(0, 1.3, 0), item, motion, 40);

                this.setDataFlag(DATA_FLAGS, DATA_FLAG_ACTION, false);
                break;

            case ProtocolInfo.TEXT_PACKET:
                if (!this.isSpawned() || !this.isAlive()) {
                    break;
                }

                this.craftingType = CRAFTING_SMALL;
                TextPacket textPacket = (TextPacket) packet;

                if (textPacket.type == TextPacket.TYPE_CHAT) {
                    textPacket.message =
                            this.removeFormat ? TextFormat.clean(textPacket.message) : textPacket.message;
                    for (String msg : textPacket.message.split("\n")) {
                        if (!msg.trim().isEmpty() && msg.length() <= 255 && this.messageCounter-- > 0) {
                            if (msg.startsWith("/")) { // Command
                                PlayerCommandPreprocessEvent commandPreprocessEvent =
                                        new PlayerCommandPreprocessEvent(this, msg);
                                if (commandPreprocessEvent.getMessage().length() > 320) {
                                    commandPreprocessEvent.setCancelled();
                                }
                                this.server.getPluginManager().callEvent(commandPreprocessEvent);
                                if (commandPreprocessEvent.isCancelled()) {
                                    break;
                                }
                                this.server.dispatchCommand(commandPreprocessEvent.getPlayer(),
                                        commandPreprocessEvent.getMessage().substring(1));
                            } else { // Chat
                                PlayerChatEvent chatEvent = new PlayerChatEvent(this, msg);
                                this.server.getPluginManager().callEvent(chatEvent);
                                if (!chatEvent.isCancelled()) {
                                    this.server.broadcastMessage(
                                            this.getServer().getLanguage().translateString(
                                                    chatEvent.getFormat(), new String[]{chatEvent.getPlayer().getDisplayName(),
                                                            chatEvent.getMessage()}),
                                            chatEvent.getRecipients());
                                }
                            }
                        }
                    }
                }
                break;
            case ProtocolInfo.CONTAINER_CLOSE_PACKET:
                ContainerClosePacket containerClosePacket = (ContainerClosePacket) packet;
                if (!this.isSpawned() || containerClosePacket.windowid == 0) {
                    break;
                }
                this.craftingType = CRAFTING_SMALL;
                this.currentTransaction = null;
                if (this.windowIndex.containsKey(containerClosePacket.windowid)) {
                    this.server.getPluginManager().callEvent(
                            new InventoryCloseEvent(this.windowIndex.get(containerClosePacket.windowid), this));
                    this.removeWindow(this.windowIndex.get(containerClosePacket.windowid));
                } else {
                    this.windowIndex.remove(containerClosePacket.windowid);
                }
                break;

            case ProtocolInfo.CRAFTING_EVENT_PACKET:
                CraftingEventPacket craftingEventPacket = (CraftingEventPacket) packet;

                if (!this.isSpawned() || !this.isAlive()) {
                    break;
                }

                Recipe recipe = this.server.getCraftingManager().getRecipe(craftingEventPacket.id);

                if (this.craftingType == CRAFTING_ANVIL) {
                    Inventory inv = this.windowIndex.get(craftingEventPacket.windowId);
                    AnvilInventory anvilInventory = inv instanceof AnvilInventory ? (AnvilInventory) inv : null;

                    if (anvilInventory == null) {

                        for (Inventory window : this.windowIndex.values()) {
                            if (window instanceof AnvilInventory) {
                                anvilInventory = (AnvilInventory) window;
                                break;
                            }
                        }

                        if (anvilInventory ==
                                null) { // If it'sf _still_ null, then the player doesn't have a valid
                            // anvil window, cannot proceed.
                            this.getServer().getLogger().debug("Couldn't find an anvil window for " +
                                    this.getName() + ", exiting");
                            this.inventory.sendContents(this);
                            break;
                        }
                    }

                    if (recipe == null) {
                        // Item renamed

                        if (!anvilInventory.onRename(this, craftingEventPacket.output[0])) {
                            this.getServer().getLogger().debug(this.getName() +
                                    " failed to rename an item in an anvil");
                            this.inventory.sendContents(this);
                        }
                    } else {
                        // TODO: Anvil crafting recipes
                    }
                    break;
                } else if (!this.windowIndex.containsKey(craftingEventPacket.windowId)) {
                    this.inventory.sendContents(this);
                    containerClosePacket = new ContainerClosePacket();
                    containerClosePacket.windowid = craftingEventPacket.windowId;
                    this.dataPacket(containerClosePacket);
                    break;
                }

                if ((recipe == null) ||
                        (((recipe instanceof BigShapelessRecipe) || (recipe instanceof BigShapedRecipe)) &&
                                this.craftingType == CRAFTING_SMALL)) {
                    this.inventory.sendContents(this);
                    break;
                }

                for (int i = 0; i < craftingEventPacket.input.length; i++) {
                    Item inputItem = craftingEventPacket.input[i];
                    if (inputItem.getDamage() == -1 || inputItem.getDamage() == 0xffff) {
                        inputItem.setDamage(null);
                    }

                    if (i < 9 && inputItem.getId() > 0) {
                        inputItem.setCount(1);
                    }
                }

                boolean canCraft = true;

                if (craftingEventPacket.input.length == 0) {
                    Recipe[] recipes =
                            getServer().getCraftingManager().getRecipesByResult(craftingEventPacket.output[0]);

                    recipe = null;

                    ArrayList<Item> ingredientz = new ArrayList<>();

                    for (Recipe r : recipes) {
                        if (r instanceof ShapedRecipe) {
                            Map<Integer, Map<Integer, Item>> ingredients = ((ShapedRecipe) r).getIngredientMap();

                            for (Map<Integer, Item> map : ingredients.values()) {
                                for (Item ingredient : map.values()) {
                                    if (!this.inventory.contains(ingredient)) {
                                        canCraft = false;
                                        break;
                                    }

                                    ingredientz.add(ingredient);
                                    this.inventory.removeItem(ingredient);
                                }
                            }

                            if (canCraft) {
                                recipe = r;
                                break;
                            }
                        }
                    }

                    if (recipe != null) {
                        CraftItemEvent craftItemEvent =
                                new CraftItemEvent(this, ingredientz.toArray(Item[]::new), recipe);
                        getServer().getPluginManager().callEvent(craftItemEvent);

                        if (craftItemEvent.isCancelled()) {
                            this.inventory.sendContents(this);
                            break;
                        }

                        this.inventory.addItem(recipe.getResult());
                    } else {
                        this.server.getLogger().debug("Unmatched desktop recipe " + craftingEventPacket.id +
                                " from player " + this.getName());
                        this.inventory.sendContents(this);
                    }
                } else {

                    if (recipe instanceof ShapedRecipe) {
                        int offsetX = 0;
                        int offsetY = 0;

                        if (this.craftingType == CRAFTING_BIG) {
                            int minX = -1, minY = -1, maxX = 0, maxY = 0;
                            for (int x = 0; x < 3; ++x) {
                                for (int y = 0; y < 3; ++y) {
                                    Item readItem = craftingEventPacket.input[y * 3 + x];
                                    if (readItem.getId() != Item.AIR) {
                                        if (minY == -1 || minY > y) {
                                            minY = y;
                                        }
                                        if (maxY < y) {
                                            maxY = y;
                                        }
                                        if (minX == -1) {
                                            minX = x;
                                        }
                                        if (maxX < x) {
                                            maxX = x;
                                        }
                                    }
                                }
                            }
                            if (maxX == minX) {
                                offsetX = minX;
                            }
                            if (maxY == minY) {
                                offsetY = minY;
                            }
                        }

                        // To fix some items can't craft
                        for (int x = 0; x < 3 - offsetX && canCraft; ++x) {
                            for (int y = 0; y < 3 - offsetY; ++y) {
                                item = craftingEventPacket.input[(y + offsetY) * 3 + (x + offsetX)];
                                Item ingredient = ((ShapedRecipe) recipe).getIngredient(x, y);
                                // todo: check this
                                // https://github.com/PocketMine/PocketMine-MP/commit/58709293cf4eee2e836a94226bbba4aca0f53908
                                if (item.getCount() > 0) {
                                    if (ingredient == null ||
                                            !ingredient.deepEquals(item, ingredient.hasMeta(),
                                                    ingredient.getCompoundTag() != null)) {
                                        canCraft = false;
                                        break;
                                    }
                                }
                            }
                        }

                        // If can't craft by auto resize, will try to craft this item in another way
                        if (!canCraft) {
                            canCraft = true;
                            for (int x = 0; x < 3 && canCraft; ++x) {
                                for (int y = 0; y < 3; ++y) {
                                    item = craftingEventPacket.input[y * 3 + x];
                                    Item ingredient = ((ShapedRecipe) recipe).getIngredient(x, y);
                                    if (item.getCount() > 0) {
                                        if (ingredient == null ||
                                                !ingredient.deepEquals(item, ingredient.hasMeta(),
                                                        ingredient.getCompoundTag() != null)) {
                                            canCraft = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                    } else if (recipe instanceof ShapelessRecipe) {
                        List<Item> needed = ((ShapelessRecipe) recipe).getIngredientList();

                        for (int x = 0; x < 3 && canCraft; ++x) {
                            for (int y = 0; y < 3; ++y) {
                                item = craftingEventPacket.input[y * 3 + x].clone();

                                for (Item n : new ArrayList<>(needed)) {
                                    if (n.deepEquals(item, n.hasMeta(), n.getCompoundTag() != null)) {
                                        int remove = Math.min(n.getCount(), item.getCount());
                                        n.setCount(n.getCount() - remove);
                                        item.setCount(item.getCount() - remove);

                                        if (n.getCount() == 0) {
                                            needed.remove(n);
                                        }
                                    }
                                }

                                if (item.getCount() > 0) {
                                    canCraft = false;
                                    break;
                                }
                            }
                        }

                        if (!needed.isEmpty()) {
                            canCraft = false;
                        }
                    } else {
                        canCraft = false;
                    }

                    List<Item> ingredientsList = new ArrayList<>();
                    if (recipe instanceof ShapedRecipe) {
                        for (int x = 0; x < 3; x++) {
                            for (int y = 0; y < 3; y++) {
                                Item need = ((ShapedRecipe) recipe).getIngredient(x, y);
                                if (need.getId() == 0) {
                                    continue;
                                }
                                for (int count = need.getCount(); count > 0; count--) {
                                    Item needAdd = need.clone();
                                    // todo: check if there need to set item's count to 1, I'm too tired to check
                                    // that today =w=
                                    needAdd.setCount(1);
                                    ingredientsList.add(needAdd);
                                }
                            }
                        }
                    }
                    if (recipe instanceof ShapelessRecipe) {
                        List<Item> recipeItem = ((ShapelessRecipe) recipe).getIngredientList();
                        for (Item need : recipeItem) {
                            if (need.getId() == 0) {
                                continue;
                            }
                            Item needAdd = need.clone();
                            // todo: check if there need to set item's count to 1, I'm too tired to check
                            // that today =w=
                            needAdd.setCount(1);
                            ingredientsList.add(needAdd);
                        }
                    }

                    Item[] ingredients = ingredientsList.toArray(Item[]::new);

                    Item result = craftingEventPacket.output[0];

                    if (!canCraft || !recipe.getResult().deepEquals(result)) {
                        this.server.getLogger().debug("Unmatched recipe " + recipe.getId() + " from player " +
                                this.getName() + ": expected " + recipe.getResult() +
                                ", got " + result +
                                ", using: " + Arrays.asList(ingredients));
                        this.inventory.sendContents(this);
                        break;
                    }

                    int[] used = new int[this.inventory.getSize()];

                    for (Item ingredient : ingredients) {
                        int slot = -1;
                        for (int index : this.inventory.getContents().keySet()) {
                            Item i = this.inventory.getContents().get(index);
                            if (ingredient.getId() != 0 && ingredient.deepEquals(i, ingredient.hasMeta()) &&
                                    (i.getCount() - used[index]) >= 1) {
                                slot = index;
                                used[index]++;
                                break;
                            }
                        }

                        if (ingredient.getId() != 0 && slot == -1) {
                            canCraft = false;
                            break;
                        }
                    }

                    if (!canCraft) {
                        this.server.getLogger().debug(
                                "Unmatched recipe " + recipe.getId() + " from player " + this.getName() +
                                        ": client does not have enough items, using: " + Arrays.asList(ingredients));
                        this.inventory.sendContents(this);
                        break;
                    }
                    CraftItemEvent craftItemEvent;
                    this.server.getPluginManager().callEvent(craftItemEvent =
                            new CraftItemEvent(this, ingredients, recipe));

                    if (craftItemEvent.isCancelled()) {
                        this.inventory.sendContents(this);
                        break;
                    }

                    for (int i = 0; i < used.length; i++) {
                        int count = used[i];
                        if (count == 0) {
                            continue;
                        }

                        item = this.inventory.getItem(i);

                        Item newItem;
                        if (item.getCount() > count) {
                            newItem = item.clone();
                            newItem.setCount(item.getCount() - count);
                        } else {
                            newItem = new ItemBlock(new BlockAir(), 0, 0);
                        }

                        this.inventory.setItem(i, newItem);
                    }

                    Item[] extraItem = this.inventory.addItem(recipe.getResult());
                    for (Item i : extraItem) {
                        this.level.dropItem(this, i);
                    }
                }
                // todo: achievement

                break;
            case ProtocolInfo.CONTAINER_SET_SLOT_PACKET:
                if (!this.isSpawned() || this.blocked || !this.isAlive()) {
                    break;
                }

                ContainerSetSlotPacket containerSetSlotPacket = (ContainerSetSlotPacket) packet;
                if (containerSetSlotPacket.slot < 0) {
                    break;
                }

                BaseTransaction transaction;
                if (containerSetSlotPacket.windowid == 0) { // Our inventory
                    if (containerSetSlotPacket.slot >= this.inventory.getSize()) {
                        break;
                    }
                    if (this.isCreative()) {
                        if (Item.getCreativeItemIndex(containerSetSlotPacket.item) != -1) {
                            this.inventory.setItem(containerSetSlotPacket.slot, containerSetSlotPacket.item);
                            this.inventory.setHotbarSlotIndex(
                                    containerSetSlotPacket.slot,
                                    containerSetSlotPacket.slot); // links hotbar[packet.slot] to slots[packet.slot]
                        }
                    }
                    transaction = new BaseTransaction(this.inventory, containerSetSlotPacket.slot,
                            this.inventory.getItem(containerSetSlotPacket.slot),
                            containerSetSlotPacket.item);
                } else if (containerSetSlotPacket.windowid ==
                        ContainerSetContentPacket.SPECIAL_ARMOR) { // Our
                    // armor
                    if (containerSetSlotPacket.slot >= 4) {
                        break;
                    }

                    transaction = new BaseTransaction(
                            this.inventory, containerSetSlotPacket.slot + this.inventory.getSize(),
                            this.inventory.getArmorItem(containerSetSlotPacket.slot), containerSetSlotPacket.item);
                } else if (this.windowIndex.containsKey(containerSetSlotPacket.windowid)) {
                    Inventory inv = this.windowIndex.get(containerSetSlotPacket.windowid);

                    if (!(inv instanceof AnvilInventory)) {
                        this.craftingType = CRAFTING_SMALL;
                    }

                    if (inv instanceof EnchantInventory && containerSetSlotPacket.item.hasEnchantments()) {
                        ((EnchantInventory) inv)
                                .onEnchant(this, inv.getItem(containerSetSlotPacket.slot),
                                        containerSetSlotPacket.item);
                    }

                    transaction = new BaseTransaction(inv, containerSetSlotPacket.slot,
                            inv.getItem(containerSetSlotPacket.slot),
                            containerSetSlotPacket.item);
                } else {
                    break;
                }

                if (transaction.getSourceItem().deepEquals(transaction.getTargetItem()) &&
                        transaction.getTargetItem().getCount() == transaction.getSourceItem().getCount()) { // No
                    // changes!
                    // No changes, just a local inventory update sent by the server
                    break;
                }

                if (this.currentTransaction == null ||
                        this.currentTransaction.getCreationTime() < (System.currentTimeMillis() - 8 * 1000)) {
                    if (this.currentTransaction != null) {
                        for (Inventory inventory : this.currentTransaction.getInventories()) {
                            if (inventory instanceof PlayerInventory) {
                                ((PlayerInventory) inventory).sendArmorContents(this);
                            }
                            inventory.sendContents(this);
                        }
                    }
                    this.currentTransaction = new SimpleTransactionGroup(this);
                }

                this.currentTransaction.addTransaction(transaction);

                if (this.currentTransaction.canExecute()) {
                    // todo achievement

                    this.currentTransaction.execute();

                    this.currentTransaction = null;
                }

                break;
            case ProtocolInfo.BLOCK_ENTITY_DATA_PACKET:
                if (!this.isSpawned() || this.blocked || !this.isAlive()) {
                    break;
                }
                BlockEntityDataPacket blockEntityDataPacket = (BlockEntityDataPacket) packet;
                this.craftingType = CRAFTING_SMALL;

                pos = new Vector3(blockEntityDataPacket.x, blockEntityDataPacket.y, blockEntityDataPacket.z);
                if (pos.distanceSquared(this) > 10000) {
                    break;
                }

                BlockEntity t = this.level.getBlockEntity(pos);
                if (t instanceof BlockEntitySign) {
                    CompoundTag nbt;
                    try {
                        nbt = NBTIO.read(blockEntityDataPacket.namedTag, ByteOrder.LITTLE_ENDIAN);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (!BlockEntity.SIGN.equals(nbt.getString("id"))) {
                        ((BlockEntitySign) t).spawnTo(this);
                    } else {
                        SignChangeEvent signChangeEvent = new SignChangeEvent(
                                t.getBlock(), this,
                                new String[]{this.removeFormat ? TextFormat.clean(nbt.getString("Text1"))
                                        : nbt.getString("Text1"),
                                        this.removeFormat ? TextFormat.clean(nbt.getString("Text2"))
                                                : nbt.getString("Text2"),
                                        this.removeFormat ? TextFormat.clean(nbt.getString("Text3"))
                                                : nbt.getString("Text3"),
                                        this.removeFormat ? TextFormat.clean(nbt.getString("Text4"))
                                                : nbt.getString("Text4")});

                        if (!t.namedTag.contains("Creator") ||
                                !Objects.equals(this.getUniqueId().toString(), t.namedTag.getString("Creator"))) {
                            signChangeEvent.setCancelled();
                        }

                        this.server.getPluginManager().callEvent(signChangeEvent);

                        if (!signChangeEvent.isCancelled()) {
                            ((BlockEntitySign) t)
                                    .setText(signChangeEvent.getLine(0), signChangeEvent.getLine(1),
                                            signChangeEvent.getLine(2), signChangeEvent.getLine(3));
                        } else {
                            ((BlockEntitySign) t).spawnTo(this);
                        }
                    }
                }
                break;
            case ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET:
                RequestChunkRadiusPacket requestChunkRadiusPacket = (RequestChunkRadiusPacket) packet;
                ChunkRadiusUpdatedPacket chunkRadiusUpdatePacket = new ChunkRadiusUpdatedPacket();
                this.chunkRadius = Math.max(5, Math.min(requestChunkRadiusPacket.radius, this.viewDistance));
                chunkRadiusUpdatePacket.radius = this.chunkRadius;
                this.dataPacket(chunkRadiusUpdatePacket);
                break;
            case ProtocolInfo.ITEM_FRAME_DROP_ITEM_PACKET:
                ItemFrameDropItemPacket itemFrameDropItemPacket = (ItemFrameDropItemPacket) packet;
                Vector3 vector3 = this.getTemporalVector().setComponents(
                        itemFrameDropItemPacket.x, itemFrameDropItemPacket.y, itemFrameDropItemPacket.z);
                BlockEntity blockEntityItemFrame = this.level.getBlockEntity(vector3);
                BlockEntityItemFrame itemFrame = (BlockEntityItemFrame) blockEntityItemFrame;
                if (itemFrame != null) {
                    Block block = itemFrame.getBlock();
                    Item itemDrop = itemFrame.getItem();
                    ItemFrameDropItemEvent itemFrameDropItemEvent =
                            new ItemFrameDropItemEvent(this, block, itemFrame, itemDrop);
                    this.server.getPluginManager().callEvent(itemFrameDropItemEvent);
                    if (!itemFrameDropItemEvent.isCancelled()) {
                        if (itemDrop.getId() != Item.AIR) {
                            vector3 = this.getTemporalVector().setComponents(itemFrame.x + 0.5, itemFrame.y,
                                    itemFrame.z + 0.5);
                            this.level.dropItem(vector3, itemDrop);
                            itemFrame.setItem(new ItemBlock(new BlockAir()));
                            itemFrame.setItemRotation(0);
                            this.getLevel().addSound(new ItemFrameItemRemovedSound(this));
                        }
                    } else {
                        itemFrame.spawnTo(this);
                    }
                }
                break;
            default:
                break;
        }
    }

    private EntityShootBowEvent getEntityShootBowEvent(Item bow, CompoundTag nbt) {
        int diff = (this.server.getTick() - this.getStartAction());
        double p = (double) diff / 20;

        double f = Math.min((p * p + p * 2) / 3, 1) * 2;
        EntityShootBowEvent entityShootBowEvent =
                new EntityShootBowEvent(this, bow, new EntityArrow(this.chunk, nbt, this, f == 2), f);

        if (f < 0.1 || diff < 5) {
            entityShootBowEvent.setCancelled();
        }
        return entityShootBowEvent;
    }

    public boolean kick() {
        return this.kick("");
    }

    public boolean kick(String reason) {
        return this.kick(reason, true);
    }

    public boolean kick(String reason, boolean isAdmin) {
        PlayerKickEvent ev;
        this.server.getPluginManager().callEvent(
                ev = new PlayerKickEvent(this, reason, this.getLeaveMessage()));
        if (!ev.isCancelled()) {
            String message;
            if (isAdmin) {
                if (!this.isBanned()) {
                    message = "Kicked by admin." + (!"".equals(reason) ? " Reason: " + reason : "");
                } else {
                    message = reason;
                }
            } else {
                if ("".equals(reason)) {
                    message = "disconnectionScreen.noReason";
                } else {
                    message = reason;
                }
            }

            this.close(ev.getQuitMessage(), message);

            return true;
        }

        return false;
    }

    @Override
    public void sendMessage(String message) {
        String[] mes = this.server.getLanguage().translateString(message).split("\\n");
        for (String m : mes) {
            if (!"".equals(m)) {
                TextPacket pk = new TextPacket();
                pk.type = TextPacket.TYPE_RAW;
                pk.message = m;
                this.dataPacket(pk);
            }
        }
    }

    @Override
    public void sendMessage(TextContainer message) {
        if (message instanceof TranslationContainer) {
            this.sendTranslation(message.getText(), ((TranslationContainer) message).getParameters());
            return;
        }
        this.sendMessage(message.getText());
    }

    public void sendTranslation(String message) {
        this.sendTranslation(message, new String[0]);
    }

    public void sendTranslation(String message, String[] parameters) {
        TextPacket pk = new TextPacket();
        if (!this.server.isLanguageForced()) {
            pk.type = TextPacket.TYPE_TRANSLATION;
            pk.message = this.server.getLanguage().translateString(message, parameters, "crimsonmc.");
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] =
                        this.server.getLanguage().translateString(parameters[i], parameters, "crimsonmc.");
            }
            pk.parameters = parameters;
        } else {
            pk.type = TextPacket.TYPE_RAW;
            pk.message = this.server.getLanguage().translateString(message, parameters);
        }
        this.dataPacket(pk);
    }

    public void sendPopup(String message) {
        this.sendPopup(message, "");
    }

    public void sendPopup(String message, String subtitle) {
        TextPacket pk = new TextPacket();
        pk.type = TextPacket.TYPE_POPUP;
        pk.source = message;
        pk.message = subtitle;
        this.dataPacket(pk);
    }

    public void sendTip(String message) {
        TextPacket pk = new TextPacket();
        pk.type = TextPacket.TYPE_TIP;
        pk.message = message;
        this.dataPacket(pk);
    }

    @Override
    public void close() {
        this.close("");
    }

    public void close(String message) {
        this.close(message, "generic");
    }

    public void close(String message, String reason) {
        this.close(message, reason, true);
    }

    public void close(String message, String reason, boolean notify) {
        this.close(new TextContainer(message), reason, notify);
    }

    public void close(TextContainer message) {
        this.close(message, "generic");
    }

    public void close(TextContainer message, String reason) {
        this.close(message, reason, true);
    }

    public void close(TextContainer message, String reason, boolean notify) {
        if (this.connected && !this.closed) {
            if (notify && !reason.isEmpty()) {
                DisconnectPacket pk = new DisconnectPacket();
                pk.message = reason;
                this.directDataPacket(pk);
            }

            this.connected = false;
            PlayerQuitEvent ev = null;
            if (this.getName() != null && !this.getName().isEmpty()) {
                this.server.getPluginManager().callEvent(
                        ev = new PlayerQuitEvent(this, message, true, reason));
                if (this.loggedIn && ev.getAutoSave()) {
                    this.save();
                }
            }

            for (ServerPlayer player : new ArrayList<>(this.server.getOnlinePlayers().values())) {
                if (!player.canSee(this)) {
                    player.showPlayer(this);
                }
            }

            this.hiddenPlayers = new HashMap<>();

            for (Inventory window : new ArrayList<>(this.windowIndex.values())) {
                this.removeWindow(window);
            }

            for (long index : new ArrayList<>(this.usedChunks.keySet())) {
                int chunkX = Level.getHashX(index);
                int chunkZ = Level.getHashZ(index);
                this.level.unregisterChunkLoader(this, chunkX, chunkZ);
                this.usedChunks.remove(index);
            }

            super.close();

            this.interfaz.close(this, notify ? reason : "");

            if (this.loggedIn) {
                this.server.removeOnlinePlayer(this);
            }

            this.loggedIn = false;

            if (ev != null && !Objects.equals(this.getUsername(), "") && this.isSpawned() &&
                    !Objects.equals(ev.getQuitMessage().toString(), "")) {
                this.server.broadcastMessage(ev.getQuitMessage());
            }

            this.server.getPluginManager().unsubscribeFromPermission(Server.BROADCAST_CHANNEL_USERS,
                    this);
            this.spawned = false;
            this.server.getLogger().info(this.getServer().getLanguage().translateString(
                    "crimsonmc.player.logOut",
                    TextFormat.AQUA + (this.getName() == null ? "" : this.getName()) + TextFormat.WHITE,
                    this.ip, String.valueOf(this.port),
                    this.getServer().getLanguage().translateString(reason)));
            this.windows = new HashMap<>();
            this.windowIndex = new HashMap<>();
            this.usedChunks = new HashMap<>();
            this.loadQueue = new HashMap<>();
            this.hasSpawned = new HashMap<>();
            this.spawnPosition = null;

            if (this.riding instanceof EntityVehicle) {
                ((EntityVehicle) this.riding).linkedEntity = null;
            }

            this.riding = null;
        }

        if (this.perm != null) {
            this.perm.clearPermissions();
            this.perm = null;
        }

        if (this.inventory != null) {
            this.inventory = null;
            this.currentTransaction = null;
        }

        this.chunk = null;

        this.server.removePlayer(this);
    }

    public void save() {
        this.save(false);
    }

    public void save(boolean async) {
        if (this.closed) {
            throw new IllegalStateException("Tried to save closed player");
        }

        super.saveNBT();

        if (this.level != null) {
            this.namedTag.putString("Level", this.level.getFolderName());
            if (this.spawnPosition != null && this.spawnPosition.getLevel() != null) {
                this.namedTag.putString("SpawnLevel", this.spawnPosition.getLevel().getFolderName());
                this.namedTag.putInt("SpawnX", (int) this.spawnPosition.x);
                this.namedTag.putInt("SpawnY", (int) this.spawnPosition.y);
                this.namedTag.putInt("SpawnZ", (int) this.spawnPosition.z);
            }

            // todo save achievement

            this.namedTag.putInt("playerGameType", this.gamemode);
            this.namedTag.putLong("lastPlayed", System.currentTimeMillis() / 1000);

            this.namedTag.putString("lastIP", this.getAddress());

            this.namedTag.putInt("EXP", this.getExperience());
            this.namedTag.putInt("expLevel", this.getExperienceLevel());

            this.namedTag.putInt("foodLevel", this.getFoodData().getLevel());
            this.namedTag.putFloat("foodSaturationLevel", this.getFoodData().getFoodSaturationLevel());

            if (!"".equals(this.getUsername()) && this.namedTag != null) {
                this.server.saveOfflinePlayerData(this.getUsername(), this.namedTag, async);
            }
        }
    }

    public String getName() {
        return this.getUsername();
    }

    @Override
    public void kill() {
        if (!this.isSpawned()) {
            return;
        }

        String message = "death.attack.generic";

        List<String> params = new ArrayList<>();
        params.add(this.getDisplayName());

        EntityDamageEvent cause = this.getLastDamageCause();

        switch (cause == null ? EntityDamageEvent.CAUSE_CUSTOM : cause.getCause()) {
            case EntityDamageEvent.CAUSE_ENTITY_ATTACK:
                if (cause instanceof EntityDamageByEntityEvent) {
                    Entity e = ((EntityDamageByEntityEvent) cause).getDamager();
                    killer = e;
                    if (e instanceof ServerPlayer) {
                        message = "death.attack.player";
                        params.add(((ServerPlayer) e).getDisplayName());
                        break;
                    } else if (e instanceof EntityLiving) {
                        message = "death.attack.mob";
                        params.add(!Objects.equals(e.getNameTag(), "") ? e.getNameTag() : e.getName());
                        break;
                    } else {
                        params.add("Unknown");
                    }
                }
                break;
            case EntityDamageEvent.CAUSE_PROJECTILE:
                if (cause instanceof EntityDamageByEntityEvent) {
                    Entity e = ((EntityDamageByEntityEvent) cause).getDamager();
                    killer = e;
                    if (e instanceof ServerPlayer) {
                        message = "death.attack.arrow";
                        params.add(((ServerPlayer) e).getDisplayName());
                    } else if (e instanceof EntityLiving) {
                        message = "death.attack.arrow";
                        params.add(!Objects.equals(e.getNameTag(), "") ? e.getNameTag() : e.getName());
                        break;
                    } else {
                        params.add("Unknown");
                    }
                }
                break;
            case EntityDamageEvent.CAUSE_SUICIDE:
                message = "death.attack.generic";
                break;
            case EntityDamageEvent.CAUSE_VOID:
                message = "death.attack.outOfWorld";
                break;
            case EntityDamageEvent.CAUSE_FALL:
                if (cause.getFinalDamage() > 2) {
                    message = "death.fell.accident.generic";
                    break;
                }
                message = "death.attack.fall";
                break;

            case EntityDamageEvent.CAUSE_SUFFOCATION:
                message = "death.attack.inWall";
                break;

            case EntityDamageEvent.CAUSE_LAVA:
                message = "death.attack.lava";
                break;

            case EntityDamageEvent.CAUSE_FIRE:
                message = "death.attack.onFire";
                break;

            case EntityDamageEvent.CAUSE_FIRE_TICK:
                message = "death.attack.inFire";
                break;

            case EntityDamageEvent.CAUSE_DROWNING:
                message = "death.attack.drown";
                break;

            case EntityDamageEvent.CAUSE_CONTACT:
                if (cause instanceof EntityDamageByBlockEvent) {
                    if (((EntityDamageByBlockEvent) cause).getDamager().getId() == Block.CACTUS) {
                        message = "death.attack.cactus";
                    }
                }
                break;

            case EntityDamageEvent.CAUSE_BLOCK_EXPLOSION:
            case EntityDamageEvent.CAUSE_ENTITY_EXPLOSION:
                if (cause instanceof EntityDamageByEntityEvent) {
                    Entity e = ((EntityDamageByEntityEvent) cause).getDamager();
                    killer = e;
                    if (e instanceof ServerPlayer) {
                        message = "death.attack.explosion.player";
                        params.add(((ServerPlayer) e).getDisplayName());
                    } else if (e instanceof EntityLiving) {
                        message = "death.attack.explosion.player";
                        params.add(!Objects.equals(e.getNameTag(), "") ? e.getNameTag() : e.getName());
                        break;
                    }
                } else {
                    message = "death.attack.explosion";
                }
                break;

            case EntityDamageEvent.CAUSE_MAGIC:
                message = "death.attack.magic";
                break;

            case EntityDamageEvent.CAUSE_CUSTOM:
                break;

            default:
        }

        this.health = 0;
        this.scheduleUpdate();

        PlayerDeathEvent ev;
        this.server.getPluginManager().callEvent(
                ev = new PlayerDeathEvent(this, this.getDrops(),
                        new TranslationContainer(message, params.toArray(String[]::new)),
                        this.getExperienceLevel()));

        if (!ev.getKeepInventory()) {
            for (Item item : ev.getDrops()) {
                this.level.dropItem(this, item);
            }

            if (this.inventory != null) {
                this.inventory.clearAll();
            }
        }

        if (!ev.getKeepExperience()) {
            if (this.isSurvival() || this.isAdventure()) {
                int exp = ev.getExperience() * 7;
                if (exp > 100)
                    exp = 100;
                int add = 1;
                for (int ii = 1; ii < exp; ii += add) {
                    this.getLevel().dropExpOrb(this, add);
                    add = new RandomUtilities().nextRange(1, 3);
                }
            }
            this.setExperience(0, 0);
        }

        if (!Objects.equals(ev.getDeathMessage().toString(), "")) {
            this.server.broadcast(ev.getDeathMessage(), Server.BROADCAST_CHANNEL_USERS);
        }

        RespawnPacket pk = new RespawnPacket();
        Position pos = this.getSpawn();
        pk.x = (float) pos.x;
        pk.y = (float) pos.y;
        pk.z = (float) pos.z;
        this.dataPacket(pk);
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        Attribute attr =
                Attribute.getAttribute(Attribute.MAX_HEALTH)
                        .setMaxValue(this.getMaxHealth())
                        .setValue(health > 0 ? (health < getMaxHealth() ? health : getMaxHealth()) : 0);
        if (this.isSpawned()) {
            UpdateAttributesPacket pk = new UpdateAttributesPacket();
            pk.entries = new Attribute[]{attr};
            pk.entityId = 0;
            this.dataPacket(pk);
        }
    }

    public int getExperience() {
        return this.exp;
    }

    // todo something on performance, lots of exp orbs then lots of packets, could
    // crash client

    public void setExperience(int exp) {
        setExperience(exp, this.getExperienceLevel());
    }

    public int getExperienceLevel() {
        return this.expLevel;
    }

    public void addExperience(int add) {
        if (add == 0)
            return;
        int now = this.getExperience();
        int added = now + add;
        int level = this.getExperienceLevel();
        int most = calculateRequireExperience(level);
        while (added >= most) { // Level Up!
            added = added - most;
            level++;
            most = calculateRequireExperience(level);
        }
        this.setExperience(added, level);
    }

    public void setExperience(int exp, int level) {
        this.exp = exp;
        this.expLevel = level;

        this.sendExperienceLevel(level);
        this.sendExperience(exp);
    }

    public void sendExperience() {
        sendExperience(this.getExperience());
    }

    public void sendExperience(int exp) {
        if (this.isSpawned()) {
            float percent = ((float) exp) / calculateRequireExperience(this.getExperienceLevel());
            this.setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(percent));
        }
    }

    public void sendExperienceLevel() {
        sendExperienceLevel(this.getExperienceLevel());
    }

    public void sendExperienceLevel(int level) {
        if (this.isSpawned()) {
            this.setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(level));
        }
    }

    public void setAttribute(Attribute attribute) {
        UpdateAttributesPacket pk = new UpdateAttributesPacket();
        pk.entries = new Attribute[]{attribute};
        pk.entityId = 0;
        this.dataPacket(pk);
    }

    @Override
    public void setMovementSpeed(float speed) {
        super.setMovementSpeed(speed);
        if (this.isSpawned()) {
            Attribute attribute = Attribute.getAttribute(Attribute.MOVEMENT_SPEED).setValue(speed);
            this.setAttribute(attribute);
        }
    }

    @Override
    public void attack(EntityDamageEvent source) {
        if (!this.isAlive()) {
            return;
        }

        if (this.isCreative() && source.getCause() != EntityDamageEvent.CAUSE_MAGIC &&
                source.getCause() != EntityDamageEvent.CAUSE_SUICIDE &&
                source.getCause() != EntityDamageEvent.CAUSE_VOID) {
            source.setCancelled();
        } else if (this.getAdventureSettings().canFly() &&
                source.getCause() == EntityDamageEvent.CAUSE_FALL) {
            source.setCancelled();
        } else if (source.getCause() == EntityDamageEvent.CAUSE_FALL) {
            if (this.getLevel().getBlock(this.getPosition().floor().add(0.5, -1, 0.5)).getId() ==
                    Block.SLIME_BLOCK) {
                if (!this.isSneaking()) {
                    source.setCancelled();
                    this.resetFallDistance();
                }
            }
        }

        if (source instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) source).getDamager();
            if (damager instanceof ServerPlayer) {
                ((ServerPlayer) damager).getFoodData().updateFoodExpLevel(0.3);
            }
            // 暴击
            boolean add = false;
            if (!damager.onGround) {
                RandomUtilities random = new RandomUtilities();
                for (int i = 0; i < 5; i++) {
                    CriticalParticle par =
                            new CriticalParticle(new Vector3(this.x + (double) random.nextRange(-15, 15) / 10,
                                    this.y + (double) random.nextRange(0, 20) / 10,
                                    this.z + (double) random.nextRange(-15, 15) / 10));
                    this.getLevel().addParticle(par);
                }

                add = true;
            }
            if (add)
                source.setDamage((float) (source.getDamage() * 1.5));
        }

        super.attack(source);

        if (!source.isCancelled() && this.getLastDamageCause() == source && this.isSpawned()) {
            this.getFoodData().updateFoodExpLevel(0.3);
            EntityEventPacket pk = new EntityEventPacket();
            pk.eid = 0;
            pk.event = EntityEventPacket.HURT_ANIMATION;
            this.dataPacket(pk);
        }
    }

    public void sendPosition(Vector3 pos) {
        this.sendPosition(pos, this.yaw);
    }

    public void sendPosition(Vector3 pos, double yaw) {
        this.sendPosition(pos, yaw, this.pitch);
    }

    public void sendPosition(Vector3 pos, double yaw, double pitch) {
        this.sendPosition(pos, yaw, pitch, MovePlayerPacket.MODE_NORMAL);
    }

    public void sendPosition(Vector3 pos, double yaw, double pitch, byte mode) {
        this.sendPosition(pos, yaw, pitch, mode, null);
    }

    public void sendPosition(Vector3 pos, double yaw, double pitch, byte mode, ServerPlayer[] targets) {
        MovePlayerPacket pk = new MovePlayerPacket();
        pk.eid = this.getId();
        pk.x = (float) pos.x;
        pk.y = (float) (pos.y + this.getEyeHeight());
        pk.z = (float) pos.z;
        pk.headYaw = (float) yaw;
        pk.pitch = (float) pitch;
        pk.yaw = (float) yaw;
        pk.mode = mode;

        if (targets != null) {
            Server.broadcastPacket(targets, pk);
        } else {
            pk.eid = 0;
            this.dataPacket(pk);
        }
    }

    @Override
    protected void checkChunks() {
        if (this.chunk == null ||
                (this.chunk.getX() != ((int) this.x >> 4) || this.chunk.getZ() != ((int) this.z >> 4))) {
            if (this.chunk != null) {
                this.chunk.removeEntity(this);
            }
            this.chunk = this.level.getChunk((int) this.x >> 4, (int) this.z >> 4, true);

            if (!this.justCreated) {
                Map<Integer, ServerPlayer> newChunk =
                        this.level.getChunkPlayers((int) this.x >> 4, (int) this.z >> 4);
                newChunk.remove(this.getLoaderId());

                // List<Player> reload = new ArrayList<>();
                for (ServerPlayer player : new ArrayList<>(this.hasSpawned.values())) {
                    if (!newChunk.containsKey(player.getLoaderId())) {
                        this.despawnFrom(player);
                    } else {
                        newChunk.remove(player.getLoaderId());
                        // reload.add(player);
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

    protected boolean checkTeleportPosition() {
        if (this.getTeleportPosition() != null) {
            int chunkX = (int) this.getTeleportPosition().x >> 4;
            int chunkZ = (int) this.getTeleportPosition().z >> 4;

            for (int X = -1; X <= 1; ++X) {
                for (int Z = -1; Z <= 1; ++Z) {
                    long index = Level.chunkHash(chunkX + X, chunkZ + Z);
                    if (!this.usedChunks.containsKey(index) || !this.usedChunks.get(index)) {
                        return false;
                    }
                }
            }

            this.sendPosition(this, this.yaw, this.pitch, MovePlayerPacket.MODE_RESET);
            this.spawnToAll();
            this.forceMovement = this.getTeleportPosition();
            this.teleportPosition = null;

            return true;
        }

        return false;
    }

    @Override
    public boolean teleport(Location location, TeleportCause cause) {
        if (!this.isOnline()) {
            return false;
        }

        Location from = this.getLocation();
        Location to = location;

        if (cause != null) {
            PlayerTeleportEvent event = new PlayerTeleportEvent(this, from, to, cause);
            this.server.getPluginManager().callEvent(event);
            if (event.isCancelled())
                return false;
            to = event.getTo();
        }

        Position oldPos = this.getPosition();
        if (super.teleport(to, null)) { // null to prevent fire of duplicate EntityTeleportEvent

            for (Inventory window : new ArrayList<>(this.windowIndex.values())) {
                if (window == this.inventory) {
                    continue;
                }
                this.removeWindow(window);
            }

            this.teleportPosition = new Vector3(this.x, this.y, this.z);

            if (!this.checkTeleportPosition()) {
                this.forceMovement = oldPos;
            } else {
                this.spawnToAll();
            }

            this.resetFallDistance();
            this.nextChunkOrderRun = 0;
            this.newPosition = null;

            // Weather
            this.getLevel().sendWeather(this);
            // Update time
            this.getLevel().sendTime(this);
            return true;
        }

        return false;
    }

    public void teleportImmediate(Location location) {
        this.teleportImmediate(location, TeleportCause.PLUGIN);
    }

    public void teleportImmediate(Location location, TeleportCause cause) {
        if (super.teleport(location, cause)) {

            for (Inventory window : new ArrayList<>(this.windowIndex.values())) {
                if (window == this.inventory) {
                    continue;
                }
                this.removeWindow(window);
            }

            this.forceMovement = new Vector3(this.x, this.y, this.z);
            this.sendPosition(this, this.yaw, this.pitch, MovePlayerPacket.MODE_RESET);

            this.resetFallDistance();
            this.orderChunks();
            this.nextChunkOrderRun = 0;
            this.newPosition = null;

            // Weather
            this.getLevel().sendWeather(this);
            // Update time
            this.getLevel().sendTime(this);
        }
    }

    public int getWindowId(Inventory inventory) {
        if (this.windows.containsKey(inventory)) {
            return this.windows.get(inventory);
        }

        return -1;
    }

    public int addWindow(Inventory inventory) {
        return this.addWindow(inventory, null);
    }

    public int addWindow(Inventory inventory, Integer forceId) {
        if (this.windows.containsKey(inventory)) {
            return this.windows.get(inventory);
        }
        int cnt;
        if (forceId == null) {
            this.windowCnt = cnt = Math.max(2, ++this.windowCnt % 99);
        } else {
            cnt = forceId;
        }
        this.windowIndex.put(cnt, inventory);
        this.windows.put(inventory, cnt);
        if (inventory.open(this)) {
            return cnt;
        } else {
            this.removeWindow(inventory);

            return -1;
        }
    }

    public void removeWindow(Inventory inventory) {
        inventory.close(this);
        if (this.windows.containsKey(inventory)) {
            int id = this.windows.get(inventory);
            this.windows.remove(this.windowIndex.get(id));
            this.windowIndex.remove(id);
        }
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getPlayerMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getPlayerMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public void onChunkChanged(FullChunk chunk) {
        this.loadQueue.put(Level.chunkHash(chunk.getX(), chunk.getZ()),
                Math.abs(((int) this.x >> 4) - chunk.getX()) +
                        Math.abs(((int) this.z >> 4) - chunk.getZ()));
    }

    @Override
    public void onChunkLoaded(FullChunk chunk) {
    }

    @Override
    public void onChunkPopulated(FullChunk chunk) {
    }

    @Override
    public void onChunkUnloaded(FullChunk chunk) {
    }

    @Override
    public void onBlockChanged(Vector3 block) {
    }

    @Override
    public Integer getLoaderId() {
        return this.loaderId;
    }

    @Override
    public boolean isLoaderActive() {
        return this.isConnected();
    }

    public boolean isFoodEnabled() {
        return !(this.isCreative() || this.isSpectator()) && this.foodEnabled;
    }

    // todo a lot on dimension

    public void setDimension() {
        ChangeDimensionPacket pk = new ChangeDimensionPacket();
        pk.dimension = (byte) (getLevel().getDimension() & 0xff);
        this.dataPacket(pk);
    }

    public synchronized Locale getLocale() {
        return this.locale.get();
    }

    public synchronized void setLocale(Locale locale) {
        this.locale.set(locale);
    }

    @Override
    public int hashCode() {
        if ((this.hash == 0) || (this.hash == 485)) {
            this.hash = (485 + (getUniqueId() != null ? getUniqueId().hashCode() : 0));
        }

        return this.hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ServerPlayer other)) {
            return false;
        }
        return Objects.equals(this.getUniqueId(), other.getUniqueId()) && this.getId() == other.getId();
    }

    public void setUsername(String username) {
        if (this.username != null)
            throw new RuntimeException("The field was already set");

        this.username = username;
    }

    public void setClientSecret(String clientSecret) {
        if (this.clientSecret != null)
            throw new RuntimeException("The field was already set");

        this.clientSecret = clientSecret;
    }

    public void setRandomClientId(long randomClientId) {
        if (this.randomClientId != -1)
            throw new RuntimeException("The field was already set");

        this.randomClientId = randomClientId;
    }

    public Vector3 getTeleportPosition() {
        return teleportPosition;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void setNewPosition(Vector3 newPosition) {
        this.newPosition = newPosition;
    }

    public boolean isRiding() {
        return this.riding != null;
    }

    public Entity getRidingEntity() {
        return this.riding;
    }

    public void sendHotbar() {
        for (int i = 0; i < getInventory().getHotbarSize(); ++i) {
            if (getInventory().getHotbarSlotIndex(i) == -1) {
                getInventory().setHeldItemIndex(i);
            }
        }
    }

    public int getStartAction() {
        return startAction;
    }

    public void setStartAction(int startAction) {
        this.startAction = startAction;
    }
}
