package org.crimsonmc.blockentity;

import org.crimsonmc.block.Block;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.nbt.tag.CompoundTag;

/**
 * Created by Snake1999 on 2016/2/3. Package cn.crimsonmc.blockentity in project crimsonmc.
 */
public class BlockEntitySkull extends BlockEntitySpawnable {

    public BlockEntitySkull(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        if (!nbt.contains("SkullType")) {
            nbt.putByte("SkullType", 0);
        }
        if (!nbt.contains("Rot")) {
            nbt.putByte("Rot", 0);
        }
        this.namedTag = nbt;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.remove("Creator");
    }

    @Override
    public boolean isBlockEntityValid() {
        return getBlock().getId() == Block.SKULL_BLOCK;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return new CompoundTag()
                .putString("id", BlockEntity.SKULL)
                .put("SkullType", this.namedTag.get("SkullType"))
                .putInt("x", (int) this.x)
                .putInt("y", (int) this.y)
                .putInt("z", (int) this.z)
                .put("Rot", this.namedTag.get("Rot"));
    }
}