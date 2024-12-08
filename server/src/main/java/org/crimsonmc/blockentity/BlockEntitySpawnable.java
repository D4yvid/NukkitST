package org.crimsonmc.blockentity;

import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.nbt.NBTIO;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.network.protocol.BlockEntityDataPacket;
import org.crimsonmc.player.ServerPlayer;

import java.io.IOException;
import java.nio.ByteOrder;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class BlockEntitySpawnable extends BlockEntity {

    public BlockEntitySpawnable(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.spawnToAll();
    }

    public abstract CompoundTag getSpawnCompound();

    public void spawnTo(ServerPlayer player) {
        if (this.closed) {
            return;
        }

        CompoundTag tag = this.getSpawnCompound();
        BlockEntityDataPacket pk = new BlockEntityDataPacket();
        pk.x = (int) this.x;
        pk.y = (int) this.y;
        pk.z = (int) this.z;
        try {
            pk.namedTag = NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.dataPacket(pk);
    }

    public void spawnToAll() {
        if (this.closed) {
            return;
        }

        for (ServerPlayer player :
                this.getLevel().getChunkPlayers(this.chunk.getX(), this.chunk.getZ()).values()) {
            if (player.isSpawned()) {
                this.spawnTo(player);
            }
        }
    }
}
