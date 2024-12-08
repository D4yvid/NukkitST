package org.crimsonmc.block;

import org.crimsonmc.blockentity.BlockEntity;
import org.crimsonmc.blockentity.BlockEntitySign;
import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.level.Level;
import org.crimsonmc.math.AxisAlignedBB;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.nbt.tag.Tag;
import org.crimsonmc.player.ServerPlayer;

/**
 * @author crimsonmc Project Team
 */
public class BlockSignPost extends BlockTransparent {

    public BlockSignPost() {
        this(0);
    }

    public BlockSignPost(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return SIGN_POST;
    }

    @Override
    public double getHardness() {
        return 1;
    }

    @Override
    public double getResistance() {
        return 5;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public String getName() {
        return "Sign Post";
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy,
                         double fz, ServerPlayer player) {
        if (face != 0) {
            CompoundTag nbt = new CompoundTag()
                    .putString("id", BlockEntity.SIGN)
                    .putInt("x", (int) block.x)
                    .putInt("y", (int) block.y)
                    .putInt("z", (int) block.z)
                    .putString("Text1", "")
                    .putString("Text2", "")
                    .putString("Text3", "")
                    .putString("Text4", "");

            if (face == 1) {
                meta = (int) Math.floor(((player.yaw + 180) * 16 / 360) + 0.5) & 0x0f;
                getLevel().setBlock(block, new BlockSignPost(meta), true);
            } else {
                meta = face;
                getLevel().setBlock(block, new BlockWallSign(meta), true);
            }

            if (player != null) {
                nbt.putString("Creator", player.getUniqueId().toString());
            }

            if (item.hasCustomBlockData()) {
                for (Tag aTag : item.getCustomBlockData().getAllTags()) {
                    nbt.put(aTag.getName(), aTag);
                }
            }

            new BlockEntitySign(getLevel().getChunk((int) block.x >> 4, (int) block.z >> 4), nbt);

            return true;
        }

        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (getSide(Vector3.SIDE_DOWN).getId() == Block.AIR) {
                getLevel().useBreakOn(this);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }

        return 0;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{Item.SIGN, 0, 1}};
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}
