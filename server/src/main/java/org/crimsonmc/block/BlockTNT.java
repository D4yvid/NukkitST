package org.crimsonmc.block;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.entity.item.EntityPrimedTNT;
import org.crimsonmc.item.Item;
import org.crimsonmc.level.Level;
import org.crimsonmc.level.sound.TNTPrimeSound;
import org.crimsonmc.math.RandomUtilities;
import org.crimsonmc.nbt.tag.CompoundTag;
import org.crimsonmc.nbt.tag.DoubleTag;
import org.crimsonmc.nbt.tag.FloatTag;
import org.crimsonmc.nbt.tag.ListTag;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created on 2015/12/8 by xtypr. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockTNT extends BlockSolid {

    public BlockTNT() {
        this(0);
    }

    public BlockTNT(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "TNT";
    }

    @Override
    public int getId() {
        return TNT;
    }

    @Override
    public double getHardness() {
        return 0;
    }

    @Override
    public double getResistance() {
        return 0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public int getBurnChance() {
        return 15;
    }

    @Override
    public int getBurnAbility() {
        return 100;
    }

    public void prime() {
        this.getLevel().setBlock(this, new BlockAir(), true);
        double mot = (new RandomUtilities()).nextSignedFloat() * Math.PI * 2;
        CompoundTag nbt =
                new CompoundTag()
                        .putList(new ListTag<DoubleTag>("Pos")
                                .add(new DoubleTag("", this.x + 0.5))
                                .add(new DoubleTag("", this.y))
                                .add(new DoubleTag("", this.z + 0.5)))
                        .putList(new ListTag<DoubleTag>("Motion")
                                .add(new DoubleTag("", -Math.sin(mot) * 0.02))
                                .add(new DoubleTag("", 0.2))
                                .add(new DoubleTag("", -Math.cos(mot) * 0.02)))
                        .putList(
                                new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0)).add(new FloatTag("", 0)))
                        .putByte("Fuse", 80);
        Entity tnt = new EntityPrimedTNT(
                this.getLevel().getChunk(this.getFloorX() >> 4, this.getFloorZ() >> 4), nbt);
        tnt.spawnToAll();
        this.level.addSound(new TNTPrimeSound(this));
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL && this.getNeighborPowerLevel() > 0) {
            this.prime();
        }
        return 0;
    }

    @Override
    public boolean onActivate(Item item, ServerPlayer player) {
        if (item.getId() == Item.FLINT_STEEL) {
            item.useOn(this);
            this.prime();
            return true;
        }
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.TNT_BLOCK_COLOR;
    }
}
