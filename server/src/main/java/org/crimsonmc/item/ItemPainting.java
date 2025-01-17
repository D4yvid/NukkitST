package org.crimsonmc.item;

import org.crimsonmc.block.Block;
import org.crimsonmc.entity.item.EntityPainting;
import org.crimsonmc.level.Level;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.nbt.tag.DoubleTag;
import org.crimsonmc.nbt.tag.FloatTag;
import org.crimsonmc.nbt.tag.ListTag;
import org.crimsonmc.player.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ItemPainting extends Item {

    public ItemPainting() {
        this(0, 1);
    }

    public ItemPainting(Integer meta) {
        this(meta, 1);
    }

    public ItemPainting(Integer meta, int count) {
        super(PAINTING, 0, count, "Painting");
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

        if (!target.isTransparent() && face > 1 && !block.isSolid()) {
            int[] direction = {2, 0, 1, 3};
            int[] right = {4, 5, 3, 2};

            List<EntityPainting.Motive> validMotives = new ArrayList<>();
            for (EntityPainting.Motive motive : EntityPainting.motives) {
                boolean valid = true;
                for (int x = 0; x < motive.width && valid; x++) {
                    for (int z = 0; z < motive.height && valid; z++) {
                        if (target.getSide(right[face - 2], x).isTransparent() ||
                                target.getSide(Vector3.SIDE_UP, z).isTransparent() ||
                                block.getSide(right[face - 2], x).isSolid() ||
                                block.getSide(Vector3.SIDE_UP, z).isSolid()) {
                            valid = false;
                        }
                    }
                }

                if (valid) {
                    validMotives.add(motive);
                }
            }

            CompoundTag nbt =
                    new CompoundTag()
                            .putByte("Direction", direction[face - 2])
                            .putString(
                                    "Motive",
                                    validMotives.get(ThreadLocalRandom.current().nextInt(validMotives.size())).title)
                            .putList(new ListTag<DoubleTag>("Pos")
                                    .add(new DoubleTag("0", target.x))
                                    .add(new DoubleTag("1", target.y))
                                    .add(new DoubleTag("2", target.z)))
                            .putList(new ListTag<DoubleTag>("Motion")
                                    .add(new DoubleTag("0", 0))
                                    .add(new DoubleTag("1", 0))
                                    .add(new DoubleTag("2", 0)))
                            .putList(new ListTag<FloatTag>("Rotation")
                                    .add(new FloatTag("0", direction[face - 2] * 90))
                                    .add(new FloatTag("1", 0)));

            EntityPainting entity = new EntityPainting(chunk, nbt);

            if (player.isSurvival()) {
                Item item = player.getInventory().getItemInHand();
                item.setCount(item.getCount() - 1);
                player.getInventory().setItemInHand(item);
            }
            entity.spawnToAll();

            return true;
        }

        return false;
    }
}
