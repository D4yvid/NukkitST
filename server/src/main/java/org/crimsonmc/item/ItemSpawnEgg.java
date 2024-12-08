package org.crimsonmc.item;

import org.crimsonmc.block.Block;
import org.crimsonmc.entity.Entity;
import org.crimsonmc.level.Level;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.nbt.tag.DoubleTag;
import org.crimsonmc.nbt.tag.FloatTag;
import org.crimsonmc.nbt.tag.ListTag;
import org.crimsonmc.player.ServerPlayer;

import java.util.Random;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemSpawnEgg extends Item {

    public ItemSpawnEgg() {
        this(0, 1);
    }

    public ItemSpawnEgg(Integer meta) {
        this(meta, 1);
    }

    public ItemSpawnEgg(Integer meta, int count) {
        super(SPAWN_EGG, meta, count, "Spawn EntityEgg");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, ServerPlayer player, Block block, Block target, int face,
                              double fx, double fy, double fz) {
        FullChunk chunk = level.getChunk((int) block.getX() >> 4, (int) block.getZ() >> 4);

        if (chunk == null) {
            return false;
        }

        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", block.getX() + 0.5))
                        .add(new DoubleTag("", block.getY()))
                        .add(new DoubleTag("", block.getZ() + 0.5)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", new Random().nextFloat() * 360))
                        .add(new FloatTag("", 0)));

        if (this.hasCustomName()) {
            nbt.putString("CustomName", this.getCustomName());
        }

        Entity entity = Entity.createEntity(this.meta, chunk, nbt);

        if (entity != null) {
            if (player.isSurvival()) {
                --this.count;
            }
            entity.spawnToAll();
            return true;
        }

        return false;
    }
}
