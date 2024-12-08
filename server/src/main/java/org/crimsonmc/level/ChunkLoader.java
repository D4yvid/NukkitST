package org.crimsonmc.level;

import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.math.Vector3;

/**
 * author: MagicDroidX crimsonmc Project
 */
public interface ChunkLoader {

    Integer getLoaderId();

    boolean isLoaderActive();

    Position getPosition();

    double getX();

    double getZ();

    Level getLevel();

    void onChunkChanged(FullChunk chunk);

    void onChunkLoaded(FullChunk chunk);

    void onChunkUnloaded(FullChunk chunk);

    void onChunkPopulated(FullChunk chunk);

    void onBlockChanged(Vector3 block);
}
