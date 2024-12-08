package org.crimsonmc.block;

import org.crimsonmc.entity.item.EntityFallingBlock;
import org.crimsonmc.item.Item;
import org.crimsonmc.level.Level;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.nbt.tag.DoubleTag;
import org.crimsonmc.nbt.tag.FloatTag;
import org.crimsonmc.nbt.tag.ListTag;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: rcsuperman crimsonmc Project
 */
public abstract class BlockFallable extends BlockSolid {

    protected BlockFallable(int meta) {
        super(meta);
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz) {
        return this.place(item, block, target, face, fx, fy, fz, null);
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        return this.getLevel().setBlock(this, this, true, true);
    }

    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            Block down = this.getSide(Vector3.SIDE_DOWN);
            if (down.getId() == AIR || down instanceof BlockLiquid) {
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<DoubleTag>("Pos")
                                .add(new DoubleTag("", this.x + 0.5))
                                .add(new DoubleTag("", this.y))
                                .add(new DoubleTag("", this.z + 0.5)))
                        .putList(new ListTag<DoubleTag>("Motion")
                                .add(new DoubleTag("", 0))
                                .add(new DoubleTag("", 0))
                                .add(new DoubleTag("", 0)))

                        .putList(new ListTag<FloatTag>("Rotation")
                                .add(new FloatTag("", 0))
                                .add(new FloatTag("", 0)))
                        .putInt("TileID", this.getId())
                        .putByte("Data", this.getDamage());

                EntityFallingBlock fall = new EntityFallingBlock(
                        this.getLevel().getChunk((int) this.x >> 4, (int) this.z >> 4), nbt);

                fall.spawnToAll();
            }
        }
        return type;
    }
}
