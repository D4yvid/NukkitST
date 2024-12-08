package org.crimsonmc.network

import org.crimsonmc.GlobalApplicationState
import org.crimsonmc.network.binary.Binary
import org.crimsonmc.network.binary.ZlibCompression.inflate
import org.crimsonmc.network.protocol.*
import org.crimsonmc.player.ServerPlayer
import org.crimsonmc.server.Server

/**
 * author: MagicDroidX crimsonmc Project
 */
class Network(server: Server) {
    val server: Server

    private val interfaces: MutableSet<SourceInterface> = HashSet()

    private val advancedInterfaces: MutableSet<AdvancedSourceInterface> = HashSet()

    private var packetPool: Array<Class<*>?> = arrayOfNulls(256)

    var upload: Double = 0.0
        private set

    var download: Double = 0.0
        private set

    private var name: String? = null

    init {
        this.registerPackets()
        this.server = server
    }

    fun addStatistics(upload: Double, download: Double) {
        this.upload += upload
        this.download += download
    }

    fun resetStatistics() {
        this.upload = 0.0
        this.download = 0.0
    }

    fun getInterfaces(): Set<SourceInterface> {
        return interfaces
    }

    fun processInterfaces() {
        for (interfaz in this.interfaces) {
            try {
                interfaz.process()
            } catch (e: Exception) {
                if (GlobalApplicationState.DEBUG > 1) {
                    server.logger.exception(e)
                }

                interfaz.emergencyShutdown()
                this.unregisterInterface(interfaz)
                server.logger.critical(
                    server.language.translateString(
                        "crimsonmc.server.networkError",
                        *arrayOf(interfaz.javaClass.name, e.message)
                    )
                )
            }
        }
    }

    fun registerInterface(interfaz: SourceInterface) {
        interfaces.add(interfaz)
        if (interfaz is AdvancedSourceInterface) {
            advancedInterfaces.add(interfaz)
            interfaz.setNetwork(this)
        }
        interfaz.setName(this.name)
    }

    fun unregisterInterface(interfaz: SourceInterface) {
        interfaces.remove(interfaz)
        advancedInterfaces.remove(interfaz)
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
        this.updateName()
    }

    fun updateName() {
        for (interfaz in this.interfaces) {
            interfaz.setName(this.name)
        }
    }

    fun registerPacket(id: Byte, clazz: Class<out DataPacket>) {
        packetPool[id.toInt() and 0xff] = clazz
    }

    fun processBatch(packet: BatchPacket, player: ServerPlayer) {
        val data: ByteArray
        try {
            data = inflate(packet.payload, 64 * 1024 * 1024)
        } catch (e: Exception) {
            Server.getInstance().logger.exception(e)
            return
        }

        val len = data.size
        var offset = 0
        try {
            val packets: MutableList<DataPacket?> = ArrayList()
            while (offset < len) {
                val pkLen = Binary.readInt(Binary.subBytes(data, offset, 4))
                offset += 4

                val buf = Binary.subBytes(data, offset, pkLen)
                offset += pkLen

                val pk: DataPacket?

                if ((getPacket(buf[0]).also { pk = it }) != null) {
                    check(pk!!.pid() != ProtocolInfo.BATCH_PACKET) { "Invalid BatchPacket inside BatchPacket" }

                    pk.setBuffer(buf, 1)

                    pk.decode()

                    packets.add(pk)

                    if (pk.getOffset() <= 0) {
                        return
                    }
                }
            }

            processPackets(player, packets)
        } catch (e: Exception) {
            if (GlobalApplicationState.DEBUG > 0) {
                server.logger.debug("BatchPacket 0x" + Binary.bytesToHexString(packet.payload))
                server.logger.exception(e)
            }
        }
    }

    /**
     * Process packets obtained from batch packets Required to perform additional analyses and filter
     * unnecessary packets
     *
     * @param packets
     */
    fun processPackets(player: ServerPlayer, packets: MutableList<DataPacket?>) {
        if (packets.isEmpty()) return
        val filter: MutableList<Byte> = ArrayList()
        for (packet in packets) {
            when (packet?.pid()) {
                ProtocolInfo.USE_ITEM_PACKET ->                     // Prevent double fire of PlayerInteractEvent
                    if (!filter.contains(ProtocolInfo.USE_ITEM_PACKET)) {
                        player.handleDataPacket(packet)
                        filter.add(ProtocolInfo.USE_ITEM_PACKET)
                    }

                else -> player.handleDataPacket(packet)
            }
        }
    }

    fun getPacket(id: Byte): DataPacket? {
        val clazz = packetPool[id.toInt() and 0xff]
        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance() as DataPacket?
            } catch (e: Exception) {
                Server.getInstance().logger.exception(e)
            }
        }
        return null
    }

    fun sendPacket(address: String?, port: Int, payload: ByteArray?) {
        for (interfaz in this.advancedInterfaces) {
            interfaz.sendRawPacket(address, port, payload)
        }
    }

    @JvmOverloads
    fun blockAddress(address: String?, timeout: Int = 300) {
        for (interfaz in this.advancedInterfaces) {
            interfaz.blockAddress(address, timeout)
        }
    }

    private fun registerPackets() {
        this.packetPool = arrayOfNulls<Class<*>>(256)

        this.registerPacket(ProtocolInfo.LOGIN_PACKET, LoginPacket::class.java)
        this.registerPacket(ProtocolInfo.PLAY_STATUS_PACKET, PlayStatusPacket::class.java)
        this.registerPacket(ProtocolInfo.DISCONNECT_PACKET, DisconnectPacket::class.java)
        this.registerPacket(ProtocolInfo.BATCH_PACKET, BatchPacket::class.java)
        this.registerPacket(ProtocolInfo.TEXT_PACKET, TextPacket::class.java)
        this.registerPacket(ProtocolInfo.SET_TIME_PACKET, SetTimePacket::class.java)
        this.registerPacket(ProtocolInfo.START_GAME_PACKET, StartGamePacket::class.java)
        this.registerPacket(ProtocolInfo.ADD_PLAYER_PACKET, AddPlayerPacket::class.java)
        this.registerPacket(ProtocolInfo.ADD_ENTITY_PACKET, AddEntityPacket::class.java)
        this.registerPacket(ProtocolInfo.REMOVE_ENTITY_PACKET, RemoveEntityPacket::class.java)
        this.registerPacket(ProtocolInfo.ADD_ITEM_ENTITY_PACKET, AddItemEntityPacket::class.java)
        this.registerPacket(ProtocolInfo.TAKE_ITEM_ENTITY_PACKET, TakeItemEntityPacket::class.java)
        this.registerPacket(ProtocolInfo.MOVE_ENTITY_PACKET, MoveEntityPacket::class.java)
        this.registerPacket(ProtocolInfo.MOVE_PLAYER_PACKET, MovePlayerPacket::class.java)
        this.registerPacket(ProtocolInfo.REMOVE_BLOCK_PACKET, RemoveBlockPacket::class.java)
        this.registerPacket(ProtocolInfo.UPDATE_BLOCK_PACKET, UpdateBlockPacket::class.java)
        this.registerPacket(ProtocolInfo.ADD_PAINTING_PACKET, AddPaintingPacket::class.java)
        this.registerPacket(ProtocolInfo.EXPLODE_PACKET, ExplodePacket::class.java)
        this.registerPacket(ProtocolInfo.LEVEL_EVENT_PACKET, LevelEventPacket::class.java)
        this.registerPacket(ProtocolInfo.BLOCK_EVENT_PACKET, BlockEventPacket::class.java)
        this.registerPacket(ProtocolInfo.ENTITY_EVENT_PACKET, EntityEventPacket::class.java)
        this.registerPacket(ProtocolInfo.MOB_EQUIPMENT_PACKET, MobEquipmentPacket::class.java)
        this.registerPacket(ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET, MobArmorEquipmentPacket::class.java)
        this.registerPacket(ProtocolInfo.INTERACT_PACKET, InteractPacket::class.java)
        this.registerPacket(ProtocolInfo.USE_ITEM_PACKET, UseItemPacket::class.java)
        this.registerPacket(ProtocolInfo.PLAYER_ACTION_PACKET, PlayerActionPacket::class.java)
        this.registerPacket(ProtocolInfo.HURT_ARMOR_PACKET, HurtArmorPacket::class.java)
        this.registerPacket(ProtocolInfo.SET_ENTITY_DATA_PACKET, SetEntityDataPacket::class.java)
        this.registerPacket(ProtocolInfo.SET_ENTITY_MOTION_PACKET, SetEntityMotionPacket::class.java)
        this.registerPacket(ProtocolInfo.SET_ENTITY_LINK_PACKET, SetEntityLinkPacket::class.java)
        // this.registerPacket(ProtocolInfo.SET_HEALTH_PACKET, SetHealthPacket.class);
        this.registerPacket(ProtocolInfo.SET_SPAWN_POSITION_PACKET, SetSpawnPositionPacket::class.java)
        this.registerPacket(ProtocolInfo.ANIMATE_PACKET, AnimatePacket::class.java)
        this.registerPacket(ProtocolInfo.RESPAWN_PACKET, RespawnPacket::class.java)
        this.registerPacket(ProtocolInfo.DROP_ITEM_PACKET, DropItemPacket::class.java)
        this.registerPacket(ProtocolInfo.CONTAINER_OPEN_PACKET, ContainerOpenPacket::class.java)
        this.registerPacket(ProtocolInfo.CONTAINER_CLOSE_PACKET, ContainerClosePacket::class.java)
        this.registerPacket(ProtocolInfo.CONTAINER_SET_SLOT_PACKET, ContainerSetSlotPacket::class.java)
        this.registerPacket(ProtocolInfo.CONTAINER_SET_DATA_PACKET, ContainerSetDataPacket::class.java)
        this.registerPacket(ProtocolInfo.CONTAINER_SET_CONTENT_PACKET, ContainerSetContentPacket::class.java)
        this.registerPacket(ProtocolInfo.CRAFTING_DATA_PACKET, CraftingDataPacket::class.java)
        this.registerPacket(ProtocolInfo.CRAFTING_EVENT_PACKET, CraftingEventPacket::class.java)
        this.registerPacket(ProtocolInfo.ADVENTURE_SETTINGS_PACKET, AdventureSettingsPacket::class.java)
        this.registerPacket(ProtocolInfo.BLOCK_ENTITY_DATA_PACKET, BlockEntityDataPacket::class.java)
        this.registerPacket(ProtocolInfo.PLAYER_INPUT_PACKET, PlayerInputPacket::class.java)
        this.registerPacket(ProtocolInfo.FULL_CHUNK_DATA_PACKET, FullChunkDataPacket::class.java)
        this.registerPacket(ProtocolInfo.SET_DIFFICULTY_PACKET, SetDifficultyPacket::class.java)
        this.registerPacket(ProtocolInfo.CHANGE_DIMENSION_PACKET, ChangeDimensionPacket::class.java)
        this.registerPacket(ProtocolInfo.SET_PLAYER_GAMETYPE_PACKET, SetPlayerGameTypePacket::class.java)
        this.registerPacket(ProtocolInfo.PLAYER_LIST_PACKET, PlayerListPacket::class.java)
        this.registerPacket(ProtocolInfo.TELEMETRY_EVENT_PACKET, TelemetryEventPacket::class.java)
        this.registerPacket(ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET, RequestChunkRadiusPacket::class.java)
        this.registerPacket(ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET, ChunkRadiusUpdatedPacket::class.java)
        this.registerPacket(ProtocolInfo.REPLACE_SELECTED_ITEM_PACKET, ReplaceSelectedItemPacket::class.java)
        this.registerPacket(ProtocolInfo.ITEM_FRAME_DROP_ITEM_PACKET, ItemFrameDropItemPacket::class.java)
    }

    companion object {
        const val CHANNEL_NONE: Byte = 0

        const val CHANNEL_PRIORITY: Byte = 1 // Priority channel, only to be used when it

        // matters
        const val CHANNEL_WORLD_CHUNKS: Byte = 2 // Chunk sending

        const val CHANNEL_MOVEMENT: Byte = 3 // Movement sending

        const val CHANNEL_BLOCKS: Byte = 4 // Block updates or explosions

        const val CHANNEL_WORLD_EVENTS: Byte = 5 // Entity, level or blockentity entity events

        const val CHANNEL_ENTITY_SPAWNING: Byte = 6 // Entity spawn/despawn channel

        const val CHANNEL_TEXT: Byte = 7 // Chat and other text stuff

        const val CHANNEL_END: Byte = 31

        @JvmField
        var BATCH_THRESHOLD: Int = 512
    }
}
