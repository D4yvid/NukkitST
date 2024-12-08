package org.crimsonmc.block;

import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemTool;
import org.crimsonmc.level.sound.NoteBoxSound;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created by Snake1999 on 2016/1/17. Package cn.crimsonmc.block in project crimsonmc.
 */
public class BlockNoteblock extends BlockSolid {

    public BlockNoteblock() {
        this(0);
    }

    public BlockNoteblock(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Note Block";
    }

    @Override
    public int getId() {
        return NOTEBLOCK;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public double getHardness() {
        return 0.8D;
    }

    @Override
    public double getResistance() {
        return 4D;
    }

    public boolean canBeActivated() {
        return true;
    }

    public int getStrength() {
        if (this.meta < 24)
            this.meta++;
        else
            this.meta = 0;
        this.getLevel().setBlock(this, this);
        return this.meta;
    }

    public int getInstrument() {
        Block below = this.getSide(Vector3.SIDE_DOWN);
        switch (below.getId()) {
            case WOODEN_PLANK:
            case NOTEBLOCK:
            case CRAFTING_TABLE:
                return NoteBoxSound.INSTRUMENT_BASS;
            case SAND:
            case SANDSTONE:
            case SOUL_SAND:
                return NoteBoxSound.INSTRUMENT_TABOUR;
            case GLASS:
            case GLASS_PANEL:
            case GLOWSTONE_BLOCK:
                return NoteBoxSound.INSTRUMENT_CLICK;
            case COAL_ORE:
            case DIAMOND_ORE:
            case EMERALD_ORE:
            case GLOWING_REDSTONE_ORE:
            case GOLD_ORE:
            case IRON_ORE:
            case LAPIS_ORE:
            case REDSTONE_ORE:
                return NoteBoxSound.INSTRUMENT_BASS_DRUM;
            default:
                return NoteBoxSound.INSTRUMENT_PIANO;
        }
    }

    public boolean onActivate(Item item) {
        return this.onActivate(item, null);
    }

    public boolean onActivate(Item item, ServerPlayer player) {
        Block up = this.getSide(Vector3.SIDE_UP);
        if (up.getId() == Block.AIR) {
            this.getLevel().addSound(new NoteBoxSound(this, this.getInstrument(), this.getStrength()));
            return true;
        } else {
            return false;
        }
    }
}
