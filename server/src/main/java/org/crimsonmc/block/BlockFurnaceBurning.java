package org.crimsonmc.block;

import org.crimsonmc.blockentity.BlockEntity;
import org.crimsonmc.blockentity.BlockEntityFurnace;
import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.nbt.tag.ListTag;
import org.crimsonmc.nbt.tag.StringTag;
import org.crimsonmc.nbt.tag.Tag;
import org.crimsonmc.player.ServerPlayer;

import java.util.Iterator;
import java.util.Map;

/**
 * author: Angelic47 crimsonmc Project
 */
public class BlockFurnaceBurning extends BlockSolid {

    public BlockFurnaceBurning() {
        this(0);
    }

    public BlockFurnaceBurning(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BURNING_FURNACE;
    }

    @Override
    public String getName() {
        return "Burning Furnace";
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 3.5;
    }

    @Override
    public double getResistance() {
        return 17.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int getLightLevel() {
        return 13;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        int[] faces = {4, 2, 5, 3};
        this.meta = faces[player != null ? player.getDirection() : 0];
        this.getLevel().setBlock(block, this, true, true);
        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<>("Items"))
                .putString("id", BlockEntity.FURNACE)
                .putInt("x", (int) this.x)
                .putInt("y", (int) this.y)
                .putInt("z", (int) this.z);

        if (item.hasCustomName()) {
            nbt.putString("CustomName", item.getCustomName());
        }

        if (item.hasCustomBlockData()) {
            Map<String, Tag> customData = item.getCustomBlockData().getTags();
            Iterator iter = customData.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry tag = (Map.Entry) iter.next();
                nbt.put((String) tag.getKey(), (Tag) tag.getValue());
            }
        }

        new BlockEntityFurnace(this.getLevel().getChunk((int) (this.x) >> 4, (int) (this.z) >> 4), nbt);

        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, new BlockAir(), true, true);
        return true;
    }

    @Override
    public boolean onActivate(Item item, ServerPlayer player) {
        if (player != null) {
            BlockEntity t = this.getLevel().getBlockEntity(this);
            BlockEntityFurnace furnace;
            if (t instanceof BlockEntityFurnace) {
                furnace = (BlockEntityFurnace) t;
            } else {
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<>("Items"))
                        .putString("id", BlockEntity.FURNACE)
                        .putInt("x", (int) this.x)
                        .putInt("y", (int) this.y)
                        .putInt("z", (int) this.z);
                furnace = new BlockEntityFurnace(
                        this.getLevel().getChunk((int) (this.x) >> 4, (int) (this.z) >> 4), nbt);
            }

            if (furnace.namedTag.contains("Lock") && furnace.namedTag.get("Lock") instanceof StringTag) {
                if (!furnace.namedTag.getString("Lock").equals(item.getCustomName())) {
                    return true;
                }
            }

            player.addWindow(furnace.getInventory());
        }

        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{{Item.FURNACE, 0, 1}};
        } else {
            return new int[0][0];
        }
    }
}
