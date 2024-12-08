package org.crimsonmc.level.format;

/**
 * author: MagicDroidX crimsonmc Project
 */
public interface Chunk extends FullChunk {

    byte SECTION_COUNT = 8;

    boolean isSectionEmpty(float fY);

    ChunkSection getSection(float fY);

    boolean setSection(float fY, ChunkSection section);

    ChunkSection[] getSections();

    class Entry {

        public final int chunkX;

        public final int chunkZ;

        public Entry(int chunkX, int chunkZ) {
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
        }
    }
}
