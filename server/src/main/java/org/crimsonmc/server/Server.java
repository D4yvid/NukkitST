package org.crimsonmc.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.crimsonmc.GlobalApplicationState;
import org.crimsonmc.block.Block;
import org.crimsonmc.blockentity.*;
import org.crimsonmc.command.*;
import org.crimsonmc.configuration.Config;
import org.crimsonmc.configuration.ConfigSection;
import org.crimsonmc.console.ServerConsole;
import org.crimsonmc.entity.Attribute;
import org.crimsonmc.entity.Entity;
import org.crimsonmc.entity.EntityHuman;
import org.crimsonmc.entity.data.Skin;
import org.crimsonmc.entity.item.*;
import org.crimsonmc.entity.mob.EntityCreeper;
import org.crimsonmc.entity.passive.*;
import org.crimsonmc.entity.projectile.EntityArrow;
import org.crimsonmc.entity.projectile.EntitySnowball;
import org.crimsonmc.entity.weather.EntityLightning;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.event.level.LevelInitEvent;
import org.crimsonmc.event.level.LevelLoadEvent;
import org.crimsonmc.event.server.QueryRegenerateEvent;
import org.crimsonmc.exception.LevelException;
import org.crimsonmc.exception.ServerException;
import org.crimsonmc.inventory.*;
import org.crimsonmc.item.Item;
import org.crimsonmc.item.enchantment.Enchantment;
import org.crimsonmc.lang.BaseLang;
import org.crimsonmc.lang.TextContainer;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.level.Level;
import org.crimsonmc.level.Position;
import org.crimsonmc.level.format.LevelProvider;
import org.crimsonmc.level.format.LevelProviderManager;
import org.crimsonmc.level.format.anvil.Anvil;
import org.crimsonmc.level.format.leveldb.LevelDB;
import org.crimsonmc.level.format.mcregion.McRegion;
import org.crimsonmc.level.generator.Flat;
import org.crimsonmc.level.generator.Generator;
import org.crimsonmc.level.generator.Normal;
import org.crimsonmc.level.generator.biome.Biome;
import org.crimsonmc.logger.ServerLogger;
import org.crimsonmc.math.MathUtilities;
import org.crimsonmc.metadata.EntityMetadataStore;
import org.crimsonmc.metadata.LevelMetadataStore;
import org.crimsonmc.metadata.PlayerMetadataStore;
import org.crimsonmc.nbt.NBTIO;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.nbt.tag.DoubleTag;
import org.crimsonmc.nbt.tag.FloatTag;
import org.crimsonmc.nbt.tag.ListTag;
import org.crimsonmc.network.CompressBatchedTask;
import org.crimsonmc.network.Network;
import org.crimsonmc.network.RakNetInterface;
import org.crimsonmc.network.SourceInterface;
import org.crimsonmc.network.binary.Binary;
import org.crimsonmc.network.binary.ZlibCompression;
import org.crimsonmc.network.protocol.BatchPacket;
import org.crimsonmc.network.protocol.CraftingDataPacket;
import org.crimsonmc.network.protocol.DataPacket;
import org.crimsonmc.network.protocol.PlayerListPacket;
import org.crimsonmc.network.query.QueryHandler;
import org.crimsonmc.network.rcon.RCON;
import org.crimsonmc.permission.BanEntry;
import org.crimsonmc.permission.BanList;
import org.crimsonmc.permission.DefaultPermissions;
import org.crimsonmc.permission.Permissible;
import org.crimsonmc.player.OfflinePlayer;
import org.crimsonmc.player.Player;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.plugin.JavaPluginLoader;
import org.crimsonmc.plugin.Plugin;
import org.crimsonmc.plugin.PluginLoadOrder;
import org.crimsonmc.plugin.PluginManager;
import org.crimsonmc.potion.Effect;
import org.crimsonmc.potion.Potion;
import org.crimsonmc.scheduler.FileWriteTask;
import org.crimsonmc.scheduler.ServerScheduler;
import org.crimsonmc.text.TextFormat;
import org.crimsonmc.thread.InterruptibleThread;
import org.crimsonmc.util.Utils;

import java.io.*;
import java.nio.ByteOrder;
import java.time.Duration;
import java.util.*;

/**
 * @author MagicDroidX
 * @author Box
 */
public class Server {

    public static final String BROADCAST_CHANNEL_ADMINISTRATIVE = "crimsonmc.broadcast.admin";

    public static final String BROADCAST_CHANNEL_USERS = "crimsonmc.broadcast.user";

    private static Server instance = null;

    private final float[] tickAverage = {20, 20, 20, 20, 20, 20, 20, 20, 20, 20,
            20, 20, 20, 20, 20, 20, 20, 20, 20, 20};

    private final float[] useAverage = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private final ServerLogger logger;

    private final String filePath;

    private final String dataPath;

    private final String pluginPath;

    private final Set<UUID> uniquePlayers = new HashSet<>();

    private final Map<String, ServerPlayer> players = new HashMap<>();

    private final Map<UUID, ServerPlayer> playerList = new HashMap<>();

    private final Map<Integer, String> identifier = new HashMap<>();

    private final Map<Integer, Level> levels = new HashMap<>();
    private final SimpleCommandMap commandMap;
    private final CraftingManager craftingManager;
    private final ConsoleCommandSender consoleSender;
    private final EntityMetadataStore entityMetadata;
    private final PlayerMetadataStore playerMetadata;
    private final LevelMetadataStore levelMetadata;
    private final Network network;
    private final BaseLang baseLang;
    private final UUID serverID;
    private final Config properties;
    private final Config config;
    private final ServerConsole console;
    private final crimsonmcConsoleThread consoleThread;
    public int networkCompressionLevel = 7;
    private BanList banByName;
    private BanList banByIP;
    private Config operators;
    private Config whitelist;
    private boolean isRunning = true;
    private boolean hasStopped = false;
    private PluginManager pluginManager = null;
    private ServerScheduler scheduler = null;
    private int tickCounter;
    private float maxTick = 20;
    private float maxUse = 0;
    private int sendUsageTicker = 0;
    private int maxPlayers;
    private boolean autoSave;
    private RCON rcon;
    private boolean networkCompressionAsync = true;
    private boolean autoTickRate = true;
    private int autoTickRateLimit = 20;
    private boolean alwaysTickPlayers = false;
    private int baseTickRate = 1;
    private Boolean getAllowFlight = null;
    private int autoSaveTicker = 0;
    private int autoSaveTicks = 6000;
    private boolean forceLanguage = false;
    private QueryHandler queryHandler;
    private QueryRegenerateEvent queryRegenerateEvent;
    private Level defaultLevel = null;

    public Server(ServerLogger logger, final String filePath, String dataPath, String pluginPath) {
        instance = this;
        this.logger = logger;

        this.filePath = filePath;
        if (!new File(dataPath + "worlds/").exists()) {
            new File(dataPath + "worlds/").mkdirs();
        }

        if (!new File(dataPath + "players/").exists()) {
            new File(dataPath + "players/").mkdirs();
        }

        if (!new File(pluginPath).exists()) {
            new File(pluginPath).mkdirs();
        }

        this.dataPath = new File(dataPath).getAbsolutePath() + "/";
        this.pluginPath = new File(pluginPath).getAbsolutePath() + "/";

        this.console = new ServerConsole(this);
        this.consoleThread = new crimsonmcConsoleThread(this.getConsole());
        this.getConsoleThread().start();

        if (!new File(this.dataPath + "crimsonmc.yml").exists()) {
            this.getLogger().info(TextFormat.GREEN + "Welcome! Please choose a language first!");
            try {
                String[] lines = Utils
                        .readFile(this.getClass().getClassLoader().getResourceAsStream(
                                "lang/language.list"))
                        .split("\n");
                for (String line : lines) {
                    this.getLogger().info(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String fallback = BaseLang.FALLBACK_LANGUAGE;
            String language = null;
            while (language == null) {
                String lang = this.getConsole().readConsoleLine();
                InputStream conf =
                        this.getClass().getClassLoader().getResourceAsStream("lang/" + lang + "/lang.ini");
                if (conf != null) {
                    language = lang;
                }
            }

            InputStream advacedConf =
                    this.getClass().getClassLoader().getResourceAsStream("lang/" + language + "/crimsonmc.yml");
            if (advacedConf == null) {
                advacedConf = this.getClass().getClassLoader().getResourceAsStream("lang/" + fallback +
                        "/crimsonmc.yml");
            }

            try {
                Utils.writeFile(this.dataPath + "crimsonmc.yml", advacedConf);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.getConsole().setExecutingCommands(true);

        this.logger.info("Loading " + TextFormat.GREEN + "crimsonmc.yml" + TextFormat.WHITE + "...");
        this.config = new Config(this.dataPath + "crimsonmc.yml", Config.YAML);

        this.logger.info("Loading " + TextFormat.GREEN + "server properties" + TextFormat.WHITE +
                "...");
        this.properties =
                new Config(this.dataPath + "server.properties", Config.PROPERTIES, new ConfigSection() {
                    {
                        put("motd", "crimsonmc Server For Minecraft: PE");
                        put("server-port", 19132);
                        put("server-ip", "0.0.0.0");
                        put("view-distance", 10);
                        put("white-list", false);
                        put("announce-player-achievements", true);
                        put("spawn-protection", 16);
                        put("max-players", 20);
                        put("allow-flight", false);
                        put("spawn-animals", true);
                        put("spawn-mobs", true);
                        put("gamemode", 0);
                        put("force-gamemode", false);
                        put("hardcore", false);
                        put("pvp", true);
                        put("difficulty", 1);
                        put("generator-settings", "");
                        put("level-name", "world");
                        put("level-seed", "");
                        put("level-type", "DEFAULT");
                        put("enable-query", true);
                        put("enable-rcon", false);
                        put("rcon.password",
                                Base64.getEncoder()
                                        .encodeToString(UUID.randomUUID().toString().replace("-", "").getBytes())
                                        .substring(3, 13));
                        put("auto-save", true);
                    }
                });

        this.forceLanguage = (Boolean) this.getConfig("settings.force-language", false);
        this.baseLang =
                new BaseLang((String) this.getConfig("settings.language", BaseLang.FALLBACK_LANGUAGE));
        this.logger.info(this.getLanguage().translateString(
                "language.selected", new String[]{getLanguage().getName(), getLanguage().getLang()}));
        this.logger.info(getLanguage().translateString(
                "crimsonmc.server.start", TextFormat.AQUA + this.getVersion() + TextFormat.WHITE));

        Object poolSize = this.getConfig("settings.async-workers", "auto");
        if (!(poolSize instanceof Integer)) {
            try {
                poolSize = Integer.valueOf((String) poolSize);
            } catch (Exception e) {
                poolSize = Math.max(Runtime.getRuntime().availableProcessors() + 1, 4);
            }
        }

        ServerScheduler.WORKERS = (int) poolSize;

        int threshold;
        try {
            threshold = Integer.valueOf(String.valueOf(this.getConfig("network.batch-threshold", 256)));
        } catch (Exception e) {
            threshold = 256;
        }

        if (threshold < 0) {
            threshold = -1;
        }

        Network.BATCH_THRESHOLD = threshold;
        this.networkCompressionLevel = (int) this.getConfig("network.compression-level", 7);
        this.networkCompressionAsync = (boolean) this.getConfig("network.async-compression", true);

        this.networkCompressionLevel = (int) this.getConfig("network.compression-level", 7);
        this.networkCompressionAsync = (boolean) this.getConfig("network.async-compression", true);

        this.autoTickRate = (boolean) this.getConfig("level-settings.auto-tick-rate", true);
        this.autoTickRateLimit = (int) this.getConfig("level-settings.auto-tick-rate-limit", 20);
        this.alwaysTickPlayers = (boolean) this.getConfig("level-settings.always-tick-players", false);
        this.baseTickRate = (int) this.getConfig("level-settings.base-tick-rate", 1);

        this.scheduler = new ServerScheduler();

        if (this.getPropertyBoolean("enable-rcon", false)) {
            this.rcon = new RCON(this, this.getPropertyString("rcon.password", ""),
                    (!this.getIp().equals("")) ? this.getIp() : "0.0.0.0",
                    this.getPropertyInt("rcon.port", this.getPort()));
        }

        this.entityMetadata = new EntityMetadataStore();
        this.playerMetadata = new PlayerMetadataStore();
        this.levelMetadata = new LevelMetadataStore();

        this.operators = new Config(this.dataPath + "ops.txt", Config.ENUM);
        this.whitelist = new Config(this.dataPath + "white-list.txt", Config.ENUM);
        this.banByName = new BanList(this.dataPath + "banned-players.json");
        this.banByName.load();
        banByIP = null;
        this.banByIP = new BanList(this.dataPath + "banned-ips.json");
        this.banByIP.load();

        this.maxPlayers = this.getPropertyInt("max-players", 20);
        this.setAutoSave(this.getPropertyBoolean("auto-save", true));

        if (this.getPropertyBoolean("hardcore", false) && this.getDifficulty() < 3) {
            this.setPropertyInt("difficulty", 3);
        }

        GlobalApplicationState.DEBUG = this.getConfig().getInt("debug.level", 1);

        if (GlobalApplicationState.DEBUG > 1) {
            this.logger.enableDebugMessages();
        } else {
            this.logger.disableDebugMessages();
        }

        this.logger.info(this.getLanguage().translateString(
                "crimsonmc.server.networkStart", new String[]{this.getIp().equals("") ? "*" : this.getIp(),
                        String.valueOf(this.getPort())}));
        this.serverID = UUID.randomUUID();

        this.network = new Network(this);
        this.network.setName(this.getMotd());

        logger.info(
            getLanguage().translateString(
                    "crimsonmc.server.info",
                    this.getName(),
                    this.getServerVersion(),
                    this.getCodename(),
                    this.getApiVersion()));

        logger.info(this.getLanguage().translateString("crimsonmc.server.license", this.getName()));

        this.consoleSender = new ConsoleCommandSender();
        this.commandMap = new SimpleCommandMap(this);

        this.registerEntities();
        this.registerBlockEntities();

        Block.init();
        Item.init();
        Biome.init();
        Effect.init();
        Potion.init();
        Enchantment.init();
        Attribute.init();

        this.craftingManager = new CraftingManager();

        this.pluginManager = new PluginManager(this, this.commandMap);
        this.pluginManager.subscribeToPermission(Server.BROADCAST_CHANNEL_ADMINISTRATIVE,
                this.consoleSender);

        this.pluginManager.registerInterface(JavaPluginLoader.class);

        this.queryRegenerateEvent = new QueryRegenerateEvent(this, 5);

        this.network.registerInterface(new RakNetInterface(this));

        this.pluginManager.loadPlugins(this.pluginPath);

        this.enablePlugins(PluginLoadOrder.STARTUP);

        LevelProviderManager.addProvider(this, Anvil.class);
        LevelProviderManager.addProvider(this, McRegion.class);
        LevelProviderManager.addProvider(this, LevelDB.class);

        Generator.addGenerator(Flat.class, "flat", Generator.TYPE_FLAT);
        Generator.addGenerator(Normal.class, "normal", Generator.TYPE_INFINITE);
        Generator.addGenerator(Normal.class, "default", Generator.TYPE_INFINITE);
        // todo: add old generator and hell generator

        for (String name : ((Map<String, Object>) this.getConfig("worlds", new HashMap<>())).keySet()) {
            if (!this.loadLevel(name)) {
                long seed;
                try {
                    seed = ((Integer) this.getConfig("worlds." + name + ".seed")).longValue();
                } catch (Exception e) {
                    seed = System.currentTimeMillis();
                }

                Map<String, Object> options = new HashMap<>();
                String[] opts = ((String) this.getConfig("worlds." + name + ".generator",
                        Generator.getGenerator("default").getSimpleName()))
                        .split(":");
                Class<? extends Generator> generator = Generator.getGenerator(opts[0]);
                if (opts.length > 1) {
                    String preset = "";
                    for (int i = 1; i < opts.length; i++) {
                        preset += opts[i] + ":";
                    }
                    preset = preset.substring(0, preset.length() - 1);

                    options.put("preset", preset);
                }

                this.generateLevel(name, seed, generator, options);
            }
        }

        if (this.getDefaultLevel() == null) {
            String defaultName = this.getPropertyString("level-name", "world");
            if (defaultName == null || "".equals(defaultName.trim())) {
                this.getLogger().warning("level-name cannot be null, using default");
                defaultName = "world";
                this.setPropertyString("level-name", defaultName);
            }

            if (!this.loadLevel(defaultName)) {
                long seed;
                String seedString =
                        String.valueOf(this.getProperty("level-seed", System.currentTimeMillis()));
                try {
                    seed = Long.valueOf(seedString);
                } catch (NumberFormatException e) {
                    seed = seedString.hashCode();
                }
                this.generateLevel(defaultName, seed == 0 ? System.currentTimeMillis() : seed);
            }

            this.setDefaultLevel(this.getLevelByName(defaultName));
        }

        this.properties.save(true);

        if (this.getDefaultLevel() == null) {
            this.getLogger().emergency(this.getLanguage().translateString("crimsonmc.level.defaultError"));
            this.forceShutdown();

            return;
        }

        if ((int) this.getConfig("ticks-per.autosave", 6000) > 0) {
            this.autoSaveTicks = (int) this.getConfig("ticks-per.autosave", 6000);
        }

        this.enablePlugins(PluginLoadOrder.POSTWORLD);

        this.start();
    }

    public static void broadcastPacket(Collection<ServerPlayer> players, DataPacket packet) {
        broadcastPacket(players.stream().toArray(ServerPlayer[]::new), packet);
    }

    public static void broadcastPacket(ServerPlayer[] players, DataPacket packet) {
        packet.encode();
        packet.isEncoded = true;
        if (Network.BATCH_THRESHOLD >= 0 && packet.getBuffer().length >= Network.BATCH_THRESHOLD) {
            Server.getInstance().batchPackets(players, new DataPacket[]{packet}, false);
            return;
        }

        for (ServerPlayer player : players) {
            player.dataPacket(packet);
        }

        if (packet.encapsulatedPacket != null) {
            packet.encapsulatedPacket = null;
        }
    }

    public static String getGamemodeString(int mode) {
        switch (mode) {
            case ServerPlayer.SURVIVAL:
                return "%gameMode.survival";
            case ServerPlayer.CREATIVE:
                return "%gameMode.creative";
            case ServerPlayer.ADVENTURE:
                return "%gameMode.adventure";
            case ServerPlayer.SPECTATOR:
                return "%gameMode.spectator";
        }
        return "UNKNOWN";
    }

    public static int getGamemodeFromString(String str) {
        switch (str.trim().toLowerCase()) {
            case "0":
            case "survival":
            case "s":
                return ServerPlayer.SURVIVAL;

            case "1":
            case "creative":
            case "c":
                return ServerPlayer.CREATIVE;

            case "2":
            case "adventure":
            case "a":
                return ServerPlayer.ADVENTURE;

            case "3":
            case "spectator":
            case "view":
            case "v":
                return ServerPlayer.SPECTATOR;
        }
        return -1;
    }

    public static int getDifficultyFromString(String str) {
        switch (str.trim().toLowerCase()) {
            case "0":
            case "peaceful":
            case "p":
                return 0;

            case "1":
            case "easy":
            case "e":
                return 1;

            case "2":
            case "normal":
            case "n":
                return 2;

            case "3":
            case "hard":
            case "h":
                return 3;
        }
        return -1;
    }

    public static Server getInstance() {
        return instance;
    }

    public ServerConsole getConsole() {
        return this.console;
    }

    public crimsonmcConsoleThread getConsoleThread() {
        return this.consoleThread;
    }

    public int broadcastMessage(String message) {
        return this.broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    public int broadcastMessage(TextContainer message) {
        return this.broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    public int broadcastMessage(String message, CommandSender[] recipients) {
        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.length;
    }

    public int broadcastMessage(String message, Collection<CommandSender> recipients) {
        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    public int broadcastMessage(TextContainer message, Collection<CommandSender> recipients) {
        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    public int broadcast(String message, String permissions) {
        Set<CommandSender> recipients = new HashSet<>();

        for (String permission : permissions.split(";")) {
            for (Permissible permissible : this.pluginManager.getPermissionSubscriptions(permission)) {
                if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                    recipients.add((CommandSender) permissible);
                }
            }
        }

        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    public int broadcast(TextContainer message, String permissions) {
        Set<CommandSender> recipients = new HashSet<>();

        for (String permission : permissions.split(";")) {
            for (Permissible permissible : this.pluginManager.getPermissionSubscriptions(permission)) {
                if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                    recipients.add((CommandSender) permissible);
                }
            }
        }

        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    public void batchPackets(ServerPlayer[] players, DataPacket[] packets) {
        this.batchPackets(players, packets, false);
    }

    public void batchPackets(ServerPlayer[] players, DataPacket[] packets, boolean forceSync) {
        if (players == null || packets == null || players.length == 0 || packets.length == 0) {
            return;
        }

        byte[][] payload = new byte[packets.length * 2][];
        for (int i = 0; i < packets.length; i++) {
            DataPacket p = packets[i];
            if (!p.isEncoded) {
                p.encode();
            }
            byte[] buf = p.getBuffer();
            payload[i * 2] = Binary.writeInt(buf.length);
            payload[i * 2 + 1] = buf;
        }
        byte[] data;
        data = Binary.appendBytes(payload);

        List<String> targets = new ArrayList<>();
        for (ServerPlayer p : players) {
            if (p.isConnected()) {
                targets.add(this.identifier.get(p.rawHashCode()));
            }
        }

        if (!forceSync && this.networkCompressionAsync) {
            this.getScheduler().scheduleAsyncTask(
                    new CompressBatchedTask(data, targets, this.networkCompressionLevel));
        } else {
            try {
                this.broadcastPacketsCallback(ZlibCompression.deflate(data, this.networkCompressionLevel), targets);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void broadcastPacketsCallback(byte[] data, List<String> identifiers) {
        BatchPacket pk = new BatchPacket();
        pk.payload = data;
        pk.encode();
        pk.isEncoded = true;

        for (String i : identifiers) {
            if (this.players.containsKey(i)) {
                this.players.get(i).dataPacket(pk);
            }
        }
    }

    public void enablePlugins(PluginLoadOrder type) {
        for (Plugin plugin : this.pluginManager.getPlugins().values()) {
            if (!plugin.isEnabled() && type == plugin.getDescription().getOrder()) {
                this.enablePlugin(plugin);
            }
        }

        if (type == PluginLoadOrder.POSTWORLD) {
            this.commandMap.registerServerAliases();
            DefaultPermissions.registerCorePermissions();
        }
    }

    public void enablePlugin(Plugin plugin) {
        this.pluginManager.enablePlugin(plugin);
    }

    public void disablePlugins() {
        this.pluginManager.disablePlugins();
    }

    public boolean dispatchCommand(CommandSender sender, String commandLine) throws ServerException {
        if (sender == null) {
            throw new ServerException("CommandSender is not valid");
        }

        if (this.commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.notFound"));

        return false;
    }

    // todo: use ticker to check console
    public ConsoleCommandSender getConsoleSender() {
        return consoleSender;
    }

    public void reload() {
        this.logger.info("Reloading...");

        this.logger.info("Saving levels...");

        for (Level level : this.levels.values()) {
            level.save();
        }

        this.pluginManager.disablePlugins();
        this.pluginManager.clearPlugins();
        this.commandMap.clearCommands();

        this.logger.info("Reloading properties...");
        this.properties.reload();
        this.maxPlayers = this.getPropertyInt("max-players", 20);

        if (this.getPropertyBoolean("hardcore", false) && this.getDifficulty() < 3) {
            this.setPropertyInt("difficulty", 3);
        }

        this.banByIP.load();
        this.banByName.load();
        this.reloadWhitelist();
        this.operators.reload();

        for (BanEntry entry : this.getIPBans().getEntires().values()) {
            this.getNetwork().blockAddress(entry.getName(), -1);
        }

        this.pluginManager.registerInterface(JavaPluginLoader.class);
        this.pluginManager.loadPlugins(this.pluginPath);
        this.enablePlugins(PluginLoadOrder.STARTUP);
        this.enablePlugins(PluginLoadOrder.POSTWORLD);
    }

    public void shutdown() {
        if (this.isRunning) {
            ServerKiller killer = new ServerKiller(90);
            killer.start();
        }
        this.isRunning = false;
    }

    public void forceShutdown() {
        if (this.hasStopped) {
            return;
        }

        try {
            if (!this.isRunning) {
                // todo sendUsage
            }

            // clean shutdown of console thread asap

            this.hasStopped = true;

            this.shutdown();

            if (this.rcon != null) {
                this.rcon.close();
            }

            this.getLogger().debug("Disabling all plugins");
            this.pluginManager.disablePlugins();

            for (ServerPlayer player : new ArrayList<>(this.players.values())) {
                player.close(player.getLeaveMessage(),
                        (String) this.getConfig("settings.shutdown-message", "Server closed"));
            }

            this.getLogger().debug("Unloading all levels");
            for (Level level : new ArrayList<>(this.getLevels().values())) {
                this.unloadLevel(level, true);
            }

            this.getLogger().debug("Removing event handlers");
            HandlerList.unregisterAll();

            this.getLogger().debug("Stopping all tasks");
            this.scheduler.cancelAllTasks();
            this.scheduler.mainThreadHeartbeat(Integer.MAX_VALUE);

            this.getLogger().debug("Closing console");
            this.getConsoleThread().interrupt();

            this.getLogger().debug("Stopping network interfaces");
            for (SourceInterface interfaz : this.network.getInterfaces()) {
                interfaz.shutdown();
                this.network.unregisterInterface(interfaz);
            }

            this.getLogger().debug("Disabling timings");
            // todo other things
        } catch (Exception e) {
            this.logger.exception(e); // todo remove this?
            this.logger.emergency("Exception happened while shutting down, exit the process");
            System.exit(1);
        }
    }

    public void start() {
        if (this.getPropertyBoolean("enable-query", true)) {
            this.queryHandler = new QueryHandler();
        }

        for (BanEntry entry : this.getIPBans().getEntires().values()) {
            this.network.blockAddress(entry.getName(), -1);
        }

        // todo send usage setting

        this.tickCounter = 0;

        this.logger.info(this.getLanguage().translateString("crimsonmc.server.defaultGameMode",
                getGamemodeString(this.getGamemode())));

        this.logger.info(this.getLanguage().translateString(
                "crimsonmc.server.startFinished",
                String.valueOf((double) (System.currentTimeMillis() - GlobalApplicationState.START_TIME) / 1000)));

        this.tickProcessor();
        this.forceShutdown();
    }

    public void handlePacket(String address, int port, byte[] payload) {
        try {
            if (payload.length > 2 &&
                    Arrays.equals(Binary.subBytes(payload, 0, 2), new byte[]{(byte) 0xfe, (byte) 0xfd}) &&
                    this.queryHandler != null) {
                this.queryHandler.handle(address, port, payload);
            }
        } catch (Exception e) {
            this.logger.exception(e);

            this.getNetwork().blockAddress(address, 600);
        }
    }

    public void tickProcessor() {
        try {
            while (this.isRunning) {
                try {
                    long timeToWait = this.tick();

                    if (timeToWait < 0) {
                        this.logger.warning(
                                String.format("Server took %d extra milliseconds to tick, did you change the time?",
                                        Math.abs(timeToWait)));
                        continue;
                    }

                    Thread.sleep(Duration.ofMillis(timeToWait));
                } catch (RuntimeException e) {
                    this.getLogger().exception(e);
                }
            }
        } catch (Throwable e) {
            this.logger.emergency("Exception happened while ticking server");
            this.logger.alert(Utils.getExceptionMessage(e));
            this.logger.alert(Utils.getAllThreadDumps());
        }
    }

    public void onPlayerLogin(ServerPlayer player) {
        if (this.sendUsageTicker > 0) {
            this.uniquePlayers.add(player.getUniqueId());
        }
    }

    public void addPlayer(String identifier, ServerPlayer player) {
        this.players.put(identifier, player);
        this.identifier.put(player.rawHashCode(), identifier);
    }

    public void addOnlinePlayer(ServerPlayer player) {
        this.addOnlinePlayer(player, true);
    }

    public void addOnlinePlayer(ServerPlayer player, boolean update) {
        this.playerList.put(player.getUniqueId(), player);

        if (update) {
            this.updatePlayerListData(player.getUniqueId(), player.getId(), player.getDisplayName(),
                    player.getSkin());
        }
    }

    public void removeOnlinePlayer(ServerPlayer player) {
        if (this.playerList.containsKey(player.getUniqueId())) {
            this.playerList.remove(player.getUniqueId());

            PlayerListPacket pk = new PlayerListPacket();
            pk.type = PlayerListPacket.TYPE_REMOVE;
            pk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(player.getUniqueId())};

            Server.broadcastPacket(this.playerList.values(), pk);
        }
    }

    public void updatePlayerListData(UUID uuid, long entityId, String name, Skin skin) {
        this.updatePlayerListData(uuid, entityId, name, skin, this.playerList.values());
    }

    public void updatePlayerListData(UUID uuid, long entityId, String name, Skin skin,
                                     ServerPlayer[] players) {
        PlayerListPacket pk = new PlayerListPacket();
        pk.type = PlayerListPacket.TYPE_ADD;
        pk.entries =
                new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid, entityId, name, skin)};
        Server.broadcastPacket(players, pk);
    }

    public void updatePlayerListData(UUID uuid, long entityId, String name, Skin skin,
                                     Collection<ServerPlayer> players) {
        this.updatePlayerListData(uuid, entityId, name, skin, players.stream().toArray(ServerPlayer[]::new));
    }

    public void removePlayerListData(UUID uuid) {
        this.removePlayerListData(uuid, this.playerList.values());
    }

    public void removePlayerListData(UUID uuid, ServerPlayer[] players) {
        PlayerListPacket pk = new PlayerListPacket();
        pk.type = PlayerListPacket.TYPE_REMOVE;
        pk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid)};
        Server.broadcastPacket(players, pk);
    }

    public void removePlayerListData(UUID uuid, Collection<ServerPlayer> players) {
        this.removePlayerListData(uuid, players.stream().toArray(ServerPlayer[]::new));
    }

    public void sendFullPlayerListData(ServerPlayer player) {
        this.sendFullPlayerListData(player, false);
    }

    public void sendFullPlayerListData(ServerPlayer player, boolean self) {
        PlayerListPacket pk = new PlayerListPacket();
        pk.type = PlayerListPacket.TYPE_ADD;
        List<PlayerListPacket.Entry> entries = new ArrayList<>();
        for (ServerPlayer p : this.playerList.values()) {
            if (!self && p == player) {
                continue;
            }

            entries.add(
                    new PlayerListPacket.Entry(p.getUniqueId(), p.getId(), p.getDisplayName(), p.getSkin()));
        }

        pk.entries = entries.stream().toArray(PlayerListPacket.Entry[]::new);

        player.dataPacket(pk);
    }

    public void sendRecipeList(ServerPlayer player) {
        CraftingDataPacket pk = new CraftingDataPacket();
        pk.cleanRecipes = true;

        for (Recipe recipe : this.getCraftingManager().getRecipes().values()) {
            if (recipe instanceof ShapedRecipe) {
                pk.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }

        for (FurnaceRecipe recipe : this.getCraftingManager().getFurnaceRecipes().values()) {
            pk.addFurnaceRecipe(recipe);
        }

        player.dataPacket(pk);
    }

    private void checkTickUpdates(int currentTick, long tickTime) {
        for (ServerPlayer p : new ArrayList<>(this.players.values())) {
            if (!p.loggedIn && (tickTime - p.creationTime) >= 10000) {
                p.close("", "Login timeout");
            } else if (this.alwaysTickPlayers) {
                p.onUpdate(currentTick);
            }
        }

        // Do level ticks
        for (Level level : this.getLevels().values()) {
            if (level.getTickRate() > this.baseTickRate && --level.tickRateCounter > 0) {
                continue;
            }

            try {
                long levelTime = System.currentTimeMillis();
                level.doTick(currentTick);
                int tickMs = (int) (System.currentTimeMillis() - levelTime);
                level.tickRateTime = tickMs;

                if (this.autoTickRate) {
                    if (tickMs < 50 && level.getTickRate() > this.baseTickRate) {
                        int r;
                        level.setTickRate(r = level.getTickRate() - 1);
                        if (r > this.baseTickRate) {
                            level.tickRateCounter = level.getTickRate();
                        }
                        this.getLogger().debug("Raising level \"" + level.getName() + "\" tick rate to " +
                                level.getTickRate() + " ticks");
                    } else if (tickMs >= 50) {
                        if (level.getTickRate() == this.baseTickRate) {
                            level.setTickRate(
                                    (int) Math.max(this.baseTickRate + 1,
                                            Math.min(this.autoTickRateLimit, Math.floor(tickMs / 50))));
                            this.getLogger().debug("Level \"" + level.getName() + "\" took " +
                                    MathUtilities.round(tickMs, 2) + "ms, setting tick rate to " +
                                    level.getTickRate() + " ticks");
                        } else if ((tickMs / level.getTickRate()) >= 50 &&
                                level.getTickRate() < this.autoTickRateLimit) {
                            level.setTickRate(level.getTickRate() + 1);
                            this.getLogger().debug("Level \"" + level.getName() + "\" took " +
                                    MathUtilities.round(tickMs, 2) + "ms, setting tick rate to " +
                                    level.getTickRate() + " ticks");
                        }
                        level.tickRateCounter = level.getTickRate();
                    }
                }
            } catch (Exception e) {
                if (GlobalApplicationState.DEBUG > 1 && this.logger != null) {
                    this.logger.exception(e);
                }

                this.logger.critical(this.getLanguage().translateString(
                        "crimsonmc.level.tickError", new String[]{level.getName(), e.toString()}));
            }
        }
    }

    public void doAutoSave() {
        if (this.getAutoSave()) {
            for (ServerPlayer player : new ArrayList<>(this.players.values())) {
                if (player.isOnline()) {
                    player.save(true);
                } else if (!player.isConnected()) {
                    this.removePlayer(player);
                }
            }

            for (Level level : this.getLevels().values()) {
                level.save();
            }
        }
    }

    private long tick() {
        long start = System.currentTimeMillis();

        ++this.tickCounter;

        this.network.processInterfaces();
        this.scheduler.mainThreadHeartbeat(this.tickCounter);

        this.checkTickUpdates(this.tickCounter, start);

        for (ServerPlayer player : new ArrayList<>(this.players.values())) {
            player.checkNetwork();
        }

        if ((this.tickCounter & 0b1111) == 0) {
            this.maxTick = 20;
            this.maxUse = 0;

            if ((this.tickCounter & 0b111111111) == 0) {
                try {
                    this.getPluginManager().callEvent(this.queryRegenerateEvent =
                            new QueryRegenerateEvent(this, 5));
                    if (this.queryHandler != null) {
                        this.queryHandler.regenerateInfo();
                    }
                } catch (Exception e) {
                    this.logger.exception(e);
                }
            }

            this.getNetwork().updateName();
        }

        if (this.autoSave && ++this.autoSaveTicker >= this.autoSaveTicks) {
            this.autoSaveTicker = 0;
            this.doAutoSave();
        }

        if (this.sendUsageTicker > 0 && --this.sendUsageTicker == 0) {
            this.sendUsageTicker = 6000;
        }

        if (this.tickCounter % 100 == 0) {
            for (Level level : this.levels.values()) {
                level.clearCache();
                level.doChunkGarbageCollection();
            }
        }

        long end = System.currentTimeMillis();

        return /* 1000 / 20 = 50; 1 second -> 20 ticks */
                50 - (end - start);
    }

    public QueryRegenerateEvent getQueryInformation() {
        return this.queryRegenerateEvent;
    }

    public String getName() {
        return "crimsonmc";
    }

    public boolean isRunning() {
        return isRunning;
    }

    public String getServerVersion() {
        return GlobalApplicationState.VERSION;
    }

    public String getCodename() {
        return GlobalApplicationState.CODENAME;
    }

    public String getVersion() {
        return GlobalApplicationState.MINECRAFT_VERSION;
    }

    public String getApiVersion() {
        return GlobalApplicationState.API_VERSION;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getPluginPath() {
        return pluginPath;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getPort() {
        return this.getPropertyInt("server-port", 19132);
    }

    public int getViewDistance() {
        return this.getPropertyInt("view-distance", 10);
    }

    public String getIp() {
        return this.getPropertyString("server-ip", "0.0.0.0");
    }

    public UUID getServerUniqueId() {
        return this.serverID;
    }

    public boolean getAutoSave() {
        return this.autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
        for (Level level : this.getLevels().values()) {
            level.setAutoSave(this.autoSave);
        }
    }

    public String getLevelType() {
        return this.getPropertyString("level-type", "DEFAULT");
    }

    public boolean getGenerateStructures() {
        return this.getPropertyBoolean("generate-structures", true);
    }

    public int getGamemode() {
        return this.getPropertyInt("gamemode", 0) & 0b11;
    }

    public boolean getForceGamemode() {
        return this.getPropertyBoolean("force-gamemode", false);
    }

    public int getDifficulty() {
        return this.getPropertyInt("difficulty", 1);
    }

    public boolean hasWhitelist() {
        return this.getPropertyBoolean("white-list", false);
    }

    public int getSpawnRadius() {
        return this.getPropertyInt("spawn-protection", 16);
    }

    public boolean getAllowFlight() {
        if (getAllowFlight == null) {
            getAllowFlight = this.getPropertyBoolean("allow-flight", false);
        }
        return getAllowFlight;
    }

    public boolean isHardcore() {
        return this.getPropertyBoolean("hardcore", false);
    }

    public int getDefaultGamemode() {
        return this.getPropertyInt("gamemode", 0);
    }

    public String getMotd() {
        return this.getPropertyString("motd", "crimsonmc Server For Minecraft: PE");
    }

    public ServerLogger getLogger() {
        return this.logger;
    }

    public EntityMetadataStore getEntityMetadata() {
        return entityMetadata;
    }

    public PlayerMetadataStore getPlayerMetadata() {
        return playerMetadata;
    }

    public LevelMetadataStore getLevelMetadata() {
        return levelMetadata;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public CraftingManager getCraftingManager() {
        return craftingManager;
    }

    public ServerScheduler getScheduler() {
        return scheduler;
    }

    public int getTick() {
        return tickCounter;
    }

    public float getTicksPerSecond() {
        return ((float) Math.round(this.maxTick * 100)) / 100;
    }

    public float getTicksPerSecondAverage() {
        float sum = 0;
        int count = this.tickAverage.length;
        for (float aTickAverage : this.tickAverage) {
            sum += aTickAverage;
        }
        return (float) MathUtilities.round(sum / count, 2);
    }

    public float getTickUsage() {
        return (float) MathUtilities.round(this.maxUse * 100, 2);
    }

    public float getTickUsageAverage() {
        float sum = 0;
        int count = this.useAverage.length;
        for (float aUseAverage : this.useAverage) {
            sum += aUseAverage;
        }
        return ((float) Math.round(sum / count * 100)) / 100;
    }

    public SimpleCommandMap getCommandMap() {
        return commandMap;
    }

    public Map<UUID, ServerPlayer> getOnlinePlayers() {
        return new HashMap<>(playerList);
    }

    public void addRecipe(Recipe recipe) {
        this.craftingManager.registerRecipe(recipe);
    }

    public Player getOfflinePlayer(String name) {
        Player result = this.getPlayerExact(name.toLowerCase());
        if (result == null) {
            return new OfflinePlayer(this, name);
        }

        return result;
    }

    public CompoundTag getOfflinePlayerData(String name) {
        name = name.toLowerCase();
        String path = this.getDataPath() + "players/";
        File file = new File(path + name + ".dat");

        if (file.exists()) {
            try {
                return NBTIO.readCompressed(new FileInputStream(file));
            } catch (Exception e) {
                file.renameTo(new File(path + name + ".dat.bak"));
                this.logger.notice(this.getLanguage().translateString("crimsonmc.data.playerCorrupted", name));
            }
        } else {
            this.logger.notice(this.getLanguage().translateString("crimsonmc.data.playerNotFound", name));
        }

        Position spawn = this.getDefaultLevel().getSafeSpawn();
        CompoundTag nbt = new CompoundTag()
                .putLong("firstPlayed", System.currentTimeMillis() / 1000)
                .putLong("lastPlayed", System.currentTimeMillis() / 1000)
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("0", spawn.x))
                        .add(new DoubleTag("1", spawn.y))
                        .add(new DoubleTag("2", spawn.z)))
                .putString("Level", this.getDefaultLevel().getName())
                .putList(new ListTag<>("Inventory"))
                .putCompound("Achievements", new CompoundTag())
                .putInt("playerGameType", this.getGamemode())
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("0", 0))
                        .add(new DoubleTag("1", 0))
                        .add(new DoubleTag("2", 0)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("0", 0))
                        .add(new FloatTag("1", 0)))
                .putFloat("FallDistance", 0)
                .putShort("Fire", 0)
                .putShort("Air", 300)
                .putBoolean("OnGround", true)
                .putBoolean("Invulnerable", false)
                .putString("NameTag", name);

        this.saveOfflinePlayerData(name, nbt);
        return nbt;
    }

    public void saveOfflinePlayerData(String name, CompoundTag tag) {
        this.saveOfflinePlayerData(name, tag, false);
    }

    public void saveOfflinePlayerData(String name, CompoundTag tag, boolean async) {
        try {
            if (async) {
                this.getScheduler().scheduleAsyncTask(
                        new FileWriteTask(this.getDataPath() + "players/" + name.toLowerCase() + ".dat",
                                NBTIO.writeGZIPCompressed(tag, ByteOrder.BIG_ENDIAN)));
            } else {
                Utils.writeFile(
                        this.getDataPath() + "players/" + name.toLowerCase() + ".dat",
                        new ByteArrayInputStream(NBTIO.writeGZIPCompressed(tag, ByteOrder.BIG_ENDIAN)));
            }
        } catch (Exception e) {
            this.logger.critical(this.getLanguage().translateString("crimsonmc.data.saveError",
                    new String[]{name, e.getMessage()}));
            if (GlobalApplicationState.DEBUG > 1) {
                this.logger.exception(e);
            }
        }
    }

    public ServerPlayer getPlayer(String name) {
        ServerPlayer found = null;
        name = name.toLowerCase();
        int delta = Integer.MAX_VALUE;
        for (ServerPlayer player : this.getOnlinePlayers().values()) {
            if (player.getName().toLowerCase().startsWith(name)) {
                int curDelta = player.getName().length() - name.length();
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                }
                if (curDelta == 0) {
                    break;
                }
            }
        }

        return found;
    }

    public ServerPlayer getPlayerExact(String name) {
        name = name.toLowerCase();
        for (ServerPlayer player : this.getOnlinePlayers().values()) {
            if (player.getName().toLowerCase().equals(name)) {
                return player;
            }
        }

        return null;
    }

    public ServerPlayer[] matchPlayer(String partialName) {
        partialName = partialName.toLowerCase();
        List<ServerPlayer> matchedPlayer = new ArrayList<>();
        for (ServerPlayer player : this.getOnlinePlayers().values()) {
            if (player.getName().toLowerCase().equals(partialName)) {
                return new ServerPlayer[]{player};
            } else if (player.getName().toLowerCase().contains(partialName)) {
                matchedPlayer.add(player);
            }
        }

        return matchedPlayer.toArray(new ServerPlayer[matchedPlayer.size()]);
    }

    public void removePlayer(ServerPlayer player) {
        if (this.identifier.containsKey(player.rawHashCode())) {
            String identifier = this.identifier.get(player.rawHashCode());
            this.players.remove(identifier);
            this.identifier.remove(player.rawHashCode());
            return;
        }

        for (String identifier : new ArrayList<>(this.players.keySet())) {
            ServerPlayer p = this.players.get(identifier);
            if (player == p) {
                this.players.remove(identifier);
                this.identifier.remove(player.rawHashCode());
                break;
            }
        }
    }

    public Map<Integer, Level> getLevels() {
        return levels;
    }

    public Level getDefaultLevel() {
        return defaultLevel;
    }

    public void setDefaultLevel(Level defaultLevel) {
        if (defaultLevel == null ||
                (this.isLevelLoaded(defaultLevel.getFolderName()) && defaultLevel != this.defaultLevel)) {
            this.defaultLevel = defaultLevel;
        }
    }

    public boolean isLevelLoaded(String name) {
        return this.getLevelByName(name) != null;
    }

    public Level getLevel(int levelId) {
        if (this.levels.containsKey(levelId)) {
            return this.levels.get(levelId);
        }
        return null;
    }

    public Level getLevelByName(String name) {
        for (Level level : this.getLevels().values()) {
            if (level.getFolderName().equals(name)) {
                return level;
            }
        }

        return null;
    }

    public boolean unloadLevel(Level level) {
        return this.unloadLevel(level, false);
    }

    public boolean unloadLevel(Level level, boolean forceUnload) {
        if (level == this.getDefaultLevel() && !forceUnload) {
            throw new IllegalStateException(
                    "The default level cannot be unloaded while running, please switch levels.");
        }

        return level.unload(forceUnload);
    }

    public boolean loadLevel(String name) {
        if (Objects.equals(name.trim(), "")) {
            throw new LevelException("Invalid empty level name");
        }
        if (this.isLevelLoaded(name)) {
            return true;
        } else if (!this.isLevelGenerated(name)) {
            this.logger.notice(this.getLanguage().translateString("crimsonmc.level.notFound", name));

            return false;
        }

        String path = this.getDataPath() + "worlds/" + name + "/";

        Class<? extends LevelProvider> provider = LevelProviderManager.getProvider(path);

        if (provider == null) {
            this.logger.error(this.getLanguage().translateString(
                    "crimsonmc.level.loadError", new String[]{name, "Unknown provider"}));

            return false;
        }

        Level level;
        try {
            level = new Level(this, name, path, provider);
        } catch (Exception e) {
            this.logger.error(this.getLanguage().translateString("crimsonmc.level.loadError",
                    new String[]{name, e.getMessage()}));
            this.logger.exception(e);
            return false;
        }

        this.levels.put(level.getId(), level);

        level.initLevel();

        this.getPluginManager().callEvent(new LevelLoadEvent(level));

        level.setTickRate(this.baseTickRate);

        return true;
    }

    public boolean generateLevel(String name) {
        return this.generateLevel(name, new java.util.Random().nextLong());
    }

    public boolean generateLevel(String name, long seed) {
        return this.generateLevel(name, seed, null);
    }

    public boolean generateLevel(String name, long seed, Class<? extends Generator> generator) {
        return this.generateLevel(name, seed, generator, new HashMap<>());
    }

    public boolean generateLevel(String name, long seed, Class<? extends Generator> generator,
                                 Map<String, Object> options) {
        if (Objects.equals(name.trim(), "") || this.isLevelGenerated(name)) {
            return false;
        }

        if (!options.containsKey("preset")) {
            options.put("preset", this.getPropertyString("generator-settings", ""));
        }

        if (generator == null) {
            generator = Generator.getGenerator(this.getLevelType());
        }

        Class<? extends LevelProvider> provider;
        String providerName;
        if ((provider = LevelProviderManager.getProviderByName(
                providerName = (String) this.getConfig("level-settings.default-format", "mcregion"))) ==
                null) {
            provider = LevelProviderManager.getProviderByName(providerName = "mcregion");
        }

        Level level;
        try {
            String path = this.getDataPath() + "worlds/" + name + "/";

            provider.getMethod("generate", String.class, String.class, long.class, Class.class, Map.class)
                    .invoke(null, path, name, seed, generator, options);

            level = new Level(this, name, path, provider);
            this.levels.put(level.getId(), level);

            level.initLevel();

            level.setTickRate(this.baseTickRate);
        } catch (Exception e) {
            this.logger.error(this.getLanguage().translateString("crimsonmc.level.generationError",
                    new String[]{name, e.getMessage()}));
            this.logger.exception(e);
            return false;
        }

        this.getPluginManager().callEvent(new LevelInitEvent(level));

        this.getPluginManager().callEvent(new LevelLoadEvent(level));

    /*this.getLogger().notice(this.getLanguage().translateString("crimsonmc.level.backgroundGeneration",
    name));

    int centerX = (int) level.getSpawnLocation().getX() >> 4;
    int centerZ = (int) level.getSpawnLocation().getZ() >> 4;

    TreeMap<String, Integer> order = new TreeMap<>();

    for (int X = -3; X <= 3; ++X) {
        for (int Z = -3; Z <= 3; ++Z) {
            int distance = X * X + Z * Z;
            int chunkX = X + centerX;
            int chunkZ = Z + centerZ;
            order.put(Level.chunkHash(chunkX, chunkZ), distance);
        }
    }

    List<Map.Entry<String, Integer>> sortList = new ArrayList<>(order.entrySet());

    Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>() {
        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            return o2.getValue() - o1.getValue();
        }
    });

    for (String index : order.keySet()) {
        Chunk.Entry entry = Level.getChunkXZ(index);
        level.populateChunk(entry.chunkX, entry.chunkZ, true);
    }*/

        return true;
    }

    public boolean isLevelGenerated(String name) {
        if (Objects.equals(name.trim(), "")) {
            return false;
        }

        String path = this.getDataPath() + "worlds/" + name + "/";
        if (this.getLevelByName(name) == null) {

            return LevelProviderManager.getProvider(path) != null;
        }

        return true;
    }

    public BaseLang getLanguage() {
        return baseLang;
    }

    public boolean isLanguageForced() {
        return forceLanguage;
    }

    public Network getNetwork() {
        return network;
    }

    // Revising later...
    public Config getConfig() {
        return this.config;
    }

    public Object getConfig(String variable) {
        return this.getConfig(variable, null);
    }

    public Object getConfig(String variable, Object defaultValue) {
        Object value = this.config.get(variable);
        return value == null ? defaultValue : value;
    }

    public Config getProperties() {
        return this.properties;
    }

    public Object getProperty(String variable) {
        return this.getProperty(variable, null);
    }

    public Object getProperty(String variable, Object defaultValue) {
        return this.properties.exists(variable) ? this.properties.get(variable) : defaultValue;
    }

    public void setPropertyString(String variable, String value) {
        this.properties.set(variable, value);
        this.properties.save();
    }

    public String getPropertyString(String variable) {
        return this.getPropertyString(variable, null);
    }

    public String getPropertyString(String variable, String defaultValue) {
        return this.properties.exists(variable) ? (String) this.properties.get(variable) : defaultValue;
    }

    public int getPropertyInt(String variable) {
        return this.getPropertyInt(variable, null);
    }

    public int getPropertyInt(String variable, Integer defaultValue) {
        return this.properties.exists(variable)
                ? (!this.properties.get(variable).equals("")
                ? Integer.parseInt(String.valueOf(this.properties.get(variable)))
                : defaultValue)
                : defaultValue;
    }

    public void setPropertyInt(String variable, int value) {
        this.properties.set(variable, value);
        this.properties.save();
    }

    public boolean getPropertyBoolean(String variable) {
        return this.getPropertyBoolean(variable, null);
    }

    public boolean getPropertyBoolean(String variable, Object defaultValue) {
        Object value = this.properties.exists(variable) ? this.properties.get(variable) : defaultValue;
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        switch (String.valueOf(value)) {
            case "on":
            case "true":
            case "1":
            case "yes":
                return true;
        }
        return false;
    }

    public void setPropertyBoolean(String variable, boolean value) {
        this.properties.set(variable, value ? "1" : "0");
        this.properties.save();
    }

    public PluginIdentifiableCommand getPluginCommand(String name) {
        Command command = this.commandMap.getCommand(name);
        if (command instanceof PluginIdentifiableCommand) {
            return (PluginIdentifiableCommand) command;
        } else {
            return null;
        }
    }

    public BanList getNameBans() {
        return this.banByName;
    }

    public BanList getIPBans() {
        return this.banByIP;
    }

    public void addOp(String name) {
        this.operators.set(name.toLowerCase(), true);
        ServerPlayer player = this.getPlayerExact(name);
        if (player != null) {
            player.recalculatePermissions();
        }
        this.operators.save(true);
    }

    public void removeOp(String name) {
        this.operators.remove(name.toLowerCase());
        ServerPlayer player = this.getPlayerExact(name);
        if (player != null) {
            player.recalculatePermissions();
        }
        this.operators.save();
    }

    public void addWhitelist(String name) {
        this.whitelist.set(name.toLowerCase(), true);
        this.whitelist.save(true);
    }

    public void removeWhitelist(String name) {
        this.whitelist.remove(name.toLowerCase());
        this.whitelist.save(true);
    }

    public boolean isWhitelisted(String name) {
        return !this.hasWhitelist() || this.operators.exists(name, true) ||
                this.whitelist.exists(name, true);
    }

    public boolean isOp(String name) {
        return this.operators.exists(name, true);
    }

    public Config getWhitelist() {
        return whitelist;
    }

    public Config getOps() {
        return operators;
    }

    public void reloadWhitelist() {
        this.whitelist.reload();
    }

    public Map<String, List<String>> getCommandAliases() {
        Object section = this.getConfig("aliases");
        Map<String, List<String>> result = new LinkedHashMap<>();
        if (section instanceof Map) {
            for (Map.Entry entry : (Set<Map.Entry>) ((Map) section).entrySet()) {
                List<String> commands = new ArrayList<>();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                if (value instanceof List) {
                    for (String string : (List<String>) value) {
                        commands.add(string);
                    }
                } else {
                    commands.add((String) value);
                }

                result.put(key, commands);
            }
        }

        return result;
    }

    private void registerEntities() {
        Entity.registerEntity("Arrow", EntityArrow.class);
        Entity.registerEntity("Item", EntityItem.class);
        Entity.registerEntity("FallingSand", EntityFallingBlock.class);
        Entity.registerEntity("PrimedTnt", EntityPrimedTNT.class);
        Entity.registerEntity("Snowball", EntitySnowball.class);
        Entity.registerEntity("Painting", EntityPainting.class);
        // todo mobs
        Entity.registerEntity("Creeper", EntityCreeper.class);
        // TODO: more mobs
        Entity.registerEntity("Chicken", EntityChicken.class);
        Entity.registerEntity("Cow", EntityCow.class);
        Entity.registerEntity("Pig", EntityPig.class);
        Entity.registerEntity("Rabbit", EntityRabbit.class);
        Entity.registerEntity("Sheep", EntitySheep.class);
        Entity.registerEntity("Wolf", EntityWolf.class);
        Entity.registerEntity("Ocelot", EntityOcelot.class);
        Entity.registerEntity("Villager", EntityVillager.class);

        Entity.registerEntity("ThrownExpBottle", EntityExpBottle.class);
        Entity.registerEntity("XpOrb", EntityXPOrb.class);
        Entity.registerEntity("ThrownPotion", EntityPotion.class);

        Entity.registerEntity("Human", EntityHuman.class, true);

        Entity.registerEntity("MinecartRideable", EntityMinecartEmpty.class);
        // TODO: 2016/1/30 all finds of minecart
        Entity.registerEntity("Boat", EntityBoat.class);

        Entity.registerEntity("Lightning", EntityLightning.class);
    }

    private void registerBlockEntities() {
        BlockEntity.registerBlockEntity(BlockEntity.FURNACE, BlockEntityFurnace.class);
        BlockEntity.registerBlockEntity(BlockEntity.CHEST, BlockEntityChest.class);
        BlockEntity.registerBlockEntity(BlockEntity.SIGN, BlockEntitySign.class);
        BlockEntity.registerBlockEntity(BlockEntity.ENCHANT_TABLE, BlockEntityEnchantTable.class);
        BlockEntity.registerBlockEntity(BlockEntity.SKULL, BlockEntitySkull.class);
        BlockEntity.registerBlockEntity(BlockEntity.FLOWER_POT, BlockEntityFlowerPot.class);
        BlockEntity.registerBlockEntity(BlockEntity.BREWING_STAND, BlockEntityBrewingStand.class);
        BlockEntity.registerBlockEntity(BlockEntity.ITEM_FRAME, BlockEntityItemFrame.class);
    }

    @Getter
    @RequiredArgsConstructor
    public static class crimsonmcConsoleThread extends Thread implements InterruptibleThread {

        private final ServerConsole console;

        @Override
        public void run() {
            this.getConsole().start();
        }
    }
}
