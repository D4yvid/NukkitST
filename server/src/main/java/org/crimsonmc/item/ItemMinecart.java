package org.crimsonmc.item;

import org.crimsonmc.block.Block;
import org.crimsonmc.entity.item.EntityMinecartEmpty;
import org.crimsonmc.level.Level;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.nbt.tag.DoubleTag;
import org.crimsonmc.nbt.tag.FloatTag;
import org.crimsonmc.nbt.tag.ListTag;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemMinecart extends Item {

    public ItemMinecart() {
        this(0, 1);
    }

    public ItemMinecart(Integer meta) {
        this(meta, 1);
    }

    public ItemMinecart(Integer meta, int count) {
        super(MINECART, meta, count, "Minecart");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, ServerPlayer player, Block block, Block target, int face,
                              double fx, double fy, double fz) {
        Block secret = level.getBlock(block.add(0, -1, 0));
        // TODO: 2016/1/30 check if blockId of secret is a rail

        EntityMinecartEmpty minecart =
                new EntityMinecartEmpty(level.getChunk(block.getFloorX() >> 4, block.getFloorZ() >> 4),
                        new CompoundTag("")
                                .putList(new ListTag<DoubleTag>("Pos")
                                        .add(new DoubleTag("", block.getX() + 0.5))
                                        .add(new DoubleTag("", block.getY()))
                                        .add(new DoubleTag("", block.getZ() + 0.5)))
                                .putList(new ListTag<DoubleTag>("Motion")
                                        .add(new DoubleTag("", 0))
                                        .add(new DoubleTag("", 0))
                                        .add(new DoubleTag("", 0)))
                                .putList(new ListTag<FloatTag>("Rotation")
                                        .add(new FloatTag("", 0))
                                        .add(new FloatTag("", 0))));
        minecart.spawnToAll();

        // TODO: 2016/1/30 if player is survival, item in hand count--

        return true;
    }
}
