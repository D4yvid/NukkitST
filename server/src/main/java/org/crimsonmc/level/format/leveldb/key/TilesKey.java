package org.crimsonmc.level.format.leveldb.key;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class TilesKey extends BaseKey {

    protected TilesKey(int chunkX, int chunkZ) {
        super(chunkX, chunkZ, DATA_TILES);
    }

    public static TilesKey create(int chunkX, int chunkZ) {
        return new TilesKey(chunkX, chunkZ);
    }
}
