package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;

/**
 * Created by Pub4Game on 03.07.2016.
 */
public class ItemFrameItemAddedSound extends GenericSound {

    public ItemFrameItemAddedSound(Vector3 pos) {
        this(pos, 0);
    }

    public ItemFrameItemAddedSound(
            Vector3 pos,
            float pitch
    ) {
        super(pos, LevelEventPacket.EVENT_SOUND_ITEM_FRAME_ITEM_ADDED, pitch);
    }

}
