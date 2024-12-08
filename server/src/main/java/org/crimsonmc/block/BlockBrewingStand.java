package org.crimsonmc.block;

import org.crimsonmc.blockentity.BlockEntity;
import org.crimsonmc.blockentity.BlockEntityBrewingStand;
import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.nbt.tag.ListTag;
import org.crimsonmc.nbt.tag.StringTag;
import org.crimsonmc.nbt.tag.Tag;
import org.crimsonmc.player.ServerPlayer;

import java.util.Iterator;
import java.util.Map;

public class BlockBrewingStand extends BlockSolid {

    public BlockBrewingStand() {
        this(0);
    }

    public BlockBrewingStand(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Brewing Stand";
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int getId() {
        return BREWING_STAND_BLOCK;
    }

    @Override
    public int getLightLevel() {
        return 1;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        if (!block.getSide(SIDE_DOWN).isTransparent()) {
            getLevel().setBlock(block, this, true, true);

            CompoundTag nbt = new CompoundTag()
                    .putList(new ListTag<>("Items"))
                    .putString("id", BlockEntity.BREWING_STAND)
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

            new BlockEntityBrewingStand(getLevel().getChunk((int) this.x >> 4, (int) this.z >> 4), nbt);

            return true;
        }
        return false;
    }

    @Override
    public boolean onActivate(Item item, ServerPlayer player) {
        if (player != null) {
            BlockEntity t = getLevel().getBlockEntity(this);
            BlockEntityBrewingStand brewing = null;
            if (t instanceof BlockEntityBrewingStand) {
                brewing = (BlockEntityBrewingStand) t;
            } else {
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<>("Items"))
                        .putString("id", BlockEntity.BREWING_STAND)
                        .putInt("x", (int) this.x)
                        .putInt("y", (int) this.y)
                        .putInt("z", (int) this.z);
                brewing = new BlockEntityBrewingStand(
                        this.getLevel().getChunk((int) (this.x) >> 4, (int) (this.z) >> 4), nbt);
            }

            if (brewing.namedTag.contains("Lock") && brewing.namedTag.get("Lock") instanceof StringTag) {
                if (!brewing.namedTag.getString("Lock").equals(item.getCustomName())) {
                    return false;
                }
            }

            player.addWindow(brewing.getInventory());
        }

        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{{Item.BREWING_STAND, 0, 1}};
        } else {
            return new int[0][0];
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }
}
