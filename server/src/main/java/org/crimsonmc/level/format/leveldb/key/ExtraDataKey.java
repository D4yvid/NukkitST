package org.crimsonmc.level.format.leveldb.key;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ExtraDataKey extends BaseKey {

    protected ExtraDataKey(int chunkX, int chunkZ) {
        super(chunkX, chunkZ, DATA_EXTRA_DATA);
    }

    public static ExtraDataKey create(int chunkX, int chunkZ) {
        return new ExtraDataKey(chunkX, chunkZ);
    }
}
