package org.crimsonmc.block;

import org.crimsonmc.blockentity.BlockEntity;
import org.crimsonmc.blockentity.BlockEntityItemFrame;
import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemBlock;
import org.crimsonmc.level.sound.ItemFrameItemAddedSound;
import org.crimsonmc.level.sound.ItemFrameItemRotated;
import org.crimsonmc.level.sound.ItemFramePlacedSound;
import org.crimsonmc.level.sound.ItemFrameRemovedSound;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.nbt.tag.Tag;
import org.crimsonmc.player.ServerPlayer;

import java.util.Random;

/**
 * Created by Pub4Game on 03.07.2016.
 */
public class BlockItemFrame extends BlockTransparent {

    public BlockItemFrame() {
        this(0);
    }

    public BlockItemFrame(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return ITEM_FRAME_BLOCK;
    }

    @Override
    public String getName() {
        return "Item Frame";
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, ServerPlayer player) {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        BlockEntityItemFrame itemFrame = (BlockEntityItemFrame) blockEntity;
        if (itemFrame.getItem().getId() == Item.AIR) {
            itemFrame.setItem(Item.get(item.getId(), item.getDamage(), 1));
            this.getLevel().addSound(new ItemFrameItemAddedSound(this));
            if (player != null && player.isSurvival()) {
                int count = item.getCount();
                if (count-- <= 0) {
                    player.getInventory().setItemInHand(new ItemBlock(new BlockAir(), 0, 0));
                    return true;
                }
                item.setCount(count);
                player.getInventory().setItemInHand(item);
            }
        } else {
            int itemRot = itemFrame.getItemRotation();
            if (itemRot >= 7) {
                itemRot = 0;
            } else {
                itemRot++;
            }
            itemFrame.setItemRotation(itemRot);
            this.getLevel().addSound(new ItemFrameItemRotated(this));
        }
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        if (!target.isTransparent() && face > 1 && !block.isSolid()) {
            switch (face) {
                case Vector3.SIDE_NORTH:
                    this.meta = 3;
                    break;
                case Vector3.SIDE_SOUTH:
                    this.meta = 2;
                    break;
                case Vector3.SIDE_WEST:
                    this.meta = 1;
                    break;
                case Vector3.SIDE_EAST:
                    this.meta = 0;
                    break;
                default:
                    return false;
            }
            this.getLevel().setBlock(block, this, true, true);
            CompoundTag nbt = new CompoundTag()
                    .putString("id", BlockEntity.ITEM_FRAME)
                    .putInt("x", (int) block.x)
                    .putInt("y", (int) block.y)
                    .putInt("z", (int) block.z)
                    .putByte("ItemRotation", 0)
                    .putFloat("ItemDropChance", 1.0f);
            if (item.hasCustomBlockData()) {
                for (Tag aTag : item.getCustomBlockData().getAllTags()) {
                    nbt.put(aTag.getName(), aTag);
                }
            }
            new BlockEntityItemFrame(this.getLevel().getChunk((int) this.x >> 4, (int) this.z >> 4), nbt);
            this.getLevel().addSound(new ItemFramePlacedSound(this));
            return true;
        }
        return false;
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, new BlockAir(), true, true);
        this.getLevel().addSound(new ItemFrameRemovedSound(this));
        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        BlockEntityItemFrame itemFrame = (BlockEntityItemFrame) blockEntity;
        int chance = new Random().nextInt(100) + 1;
        if (itemFrame != null && chance <= (itemFrame.getItemDropChance() * 100)) {
            return new int[][]{{Item.ITEM_FRAME, 0, 1},
                    {itemFrame.getItem().getId(), itemFrame.getItem().getDamage(), 1}};
        } else {
            return new int[][]{{Item.ITEM_FRAME, 0, 1}};
        }
    }
}
