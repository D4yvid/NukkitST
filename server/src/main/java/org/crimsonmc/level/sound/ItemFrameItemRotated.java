package org.crimsonmc.level.sound;

import org.crimsonmc.math.Vector3;
import org.crimsonmc.network.protocol.LevelEventPacket;

/**
 * Created by Pub4Game on 03.07.2016.
 */
public class ItemFrameItemRotated extends GenericSound {

    public ItemFrameItemRotated(Vector3 pos) {
        this(pos, 0);
    }

    public ItemFrameItemRotated(Vector3 pos, float pitch) {
        super(pos, LevelEventPacket.EVENT_SOUND_ITEM_FRAME_ITEM_ROTATED, pitch);
    }
}