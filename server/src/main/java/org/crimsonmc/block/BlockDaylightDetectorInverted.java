package org.crimsonmc.block;

import org.crimsonmc.item.Item;

/**
 * Created on 2015/11/22 by CreeperFace. Package cn.crimsonmc.block in project crimsonmc .
 */
public class BlockDaylightDetectorInverted extends BlockDaylightDetector {

    public BlockDaylightDetectorInverted() {
        this(0);
    }

    public BlockDaylightDetectorInverted(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return DAYLIGHT_DETECTOR_INVERTED;
    }

    @Override
    public String getName() {
        return "Daylight Detector Inverted";
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{Item.DAYLIGHT_DETECTOR_INVERTED, 0, 1}};
    }

    protected boolean invertDetect() {
        return true;
    }
}
