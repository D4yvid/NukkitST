package org.crimsonmc.block;

import org.crimsonmc.blockentity.BlockEntity;
import org.crimsonmc.blockentity.BlockEntityEnchantTable;
import org.crimsonmc.inventory.EnchantInventory;
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
 * Created on 2015/11/22 by CreeperFace. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockEnchantingTable extends BlockSolid {

    public BlockEnchantingTable() {
        this(0);
    }

    public BlockEnchantingTable(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return ENCHANTING_TABLE;
    }

    @Override
    public String getName() {
        return "Enchanting Table";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public double getHardness() {
        return 5;
    }

    @Override
    public double getResistance() {
        return 6000;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{{Item.ENCHANTING_TABLE, 0, 1}};
        } else {
            return new int[0][0];
        }
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        this.getLevel().setBlock(block, this, true, true);

        CompoundTag nbt = new CompoundTag()
                .putString("id", BlockEntity.ENCHANT_TABLE)
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

        BlockEntity.createBlockEntity(BlockEntity.ENCHANT_TABLE,
                getLevel().getChunk((int) this.x >> 4, (int) this.z >> 4), nbt);

        return true;
    }

    @Override
    public boolean onActivate(Item item, ServerPlayer player) {
        if (player != null) {
            BlockEntity t = this.getLevel().getBlockEntity(this);
            BlockEntityEnchantTable enchantTable;
            if (t instanceof BlockEntityEnchantTable) {
                enchantTable = (BlockEntityEnchantTable) t;
            } else {
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<>("Items"))
                        .putString("id", BlockEntity.ENCHANT_TABLE)
                        .putInt("x", (int) this.x)
                        .putInt("y", (int) this.y)
                        .putInt("z", (int) this.z);
                enchantTable = new BlockEntityEnchantTable(
                        this.getLevel().getChunk((int) (this.x) >> 4, (int) (this.z) >> 4), nbt);
            }

            if (enchantTable.namedTag.contains("Lock") && enchantTable.namedTag.get("Lock") instanceof
                    StringTag) {
                if (!enchantTable.namedTag.getString("Lock").equals(item.getCustomName())) {
                    return true;
                }
            }

            player.addWindow(new EnchantInventory(this.getLocation()));
        }

        return true;
    }
}
