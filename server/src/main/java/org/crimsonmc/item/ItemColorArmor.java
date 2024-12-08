package org.crimsonmc.item;

import org.crimsonmc.block.BlockColor;
import org.crimsonmc.nbt.tag.CompoundTag;

/**
 * Created by fromgate on 27.03.2016.
 */
abstract public class ItemColorArmor extends ItemArmor {

    public ItemColorArmor(int id) {
        super(id);
    }

    public ItemColorArmor(int id, Integer meta) {
        super(id, meta);
    }

    public ItemColorArmor(int id, Integer meta, int count) {
        super(id, meta, count);
    }

    public ItemColorArmor(int id, Integer meta, int count, String name) {
        super(id, meta, count, name);
    }

    /**
     * Set leather armor color
     *
     * @param r - red
     * @param g - green
     * @param b - blue
     * @return - Return colored item
     */
    public ItemColorArmor setColor(int r, int g, int b) {
        int rgb = r << 16 | g << 8 | b;
        CompoundTag tag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        tag.putInt("customColor", rgb);
        this.setNamedTag(tag);
        return this;
    }

    /**
     * Get color of Leather Item
     *
     * @return - BlockColor, or null if item has no color
     */
    public BlockColor getColor() {
        if (!this.hasCompoundTag())
            return null;
        CompoundTag tag = this.getNamedTag();
        if (!tag.exist("customColor"))
            return null;
        int rgb = tag.getInt("customColor");
        return new BlockColor(rgb);
    }

    /**
     * Set leather armor color
     *
     * @param dyeColor - Dye color data value
     * @return - Return colored item
     */
    @Deprecated
    public ItemColorArmor setColor(int dyeColor) {
        BlockColor blockMapColor = ItemDye.DyeColor.getByDyeData(dyeColor).getColor();
        return setColor(blockMapColor.getRed(), blockMapColor.getGreen(), blockMapColor.getBlue());
    }

    /**
     * Set leather armor color
     *
     * @param dyeColor - DyeColor object
     * @return - Return colored item
     */
    public ItemColorArmor setColor(ItemDye.DyeColor dyeColor) {
        BlockColor blockMapColor = dyeColor.getColor();
        return setColor(blockMapColor.getRed(), blockMapColor.getGreen(), blockMapColor.getBlue());
    }

    /**
     * Set leather armor color
     *
     * @param color - BlockColor object
     * @return - Return colored item
     */
    public ItemColorArmor setColor(BlockColor color) {
        return setColor(color.getRed(), color.getGreen(), color.getBlue());
    }
}
