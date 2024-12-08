package org.crimsonmc.level.sound;

import org.crimsonmc.math.Vector3;
import org.crimsonmc.network.protocol.LevelEventPacket;

/**
 * Created by Pub4Game on 28.06.2016.
 */
public class ClickFailSound extends GenericSound {

    public ClickFailSound(Vector3 pos) {
        this(pos, 0);
    }

    public ClickFailSound(Vector3 pos, float pitch) {
        super(pos, LevelEventPacket.EVENT_SOUND_CLICK_FAIL, pitch);
    }
}