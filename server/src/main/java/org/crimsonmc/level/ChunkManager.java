package org.crimsonmc.level;

import org.crimsonmc.level.format.generic.BaseFullChunk;

/**
 * author: MagicDroidX crimsonmc Project
 */
public interface ChunkManager {

    int getBlockIdAt(int x, int y, int z);

    void setBlockIdAt(int x, int y, int z, int id);

    int getBlockDataAt(int x, int y, int z);

    void setBlockDataAt(int x, int y, int z, int data);

    BaseFullChunk getChunk(int chunkX, int chunkZ);

    void setChunk(int chunkX, int chunkZ);

    void setChunk(int chunkX, int chunkZ, BaseFullChunk chunk);

    long getSeed();
}