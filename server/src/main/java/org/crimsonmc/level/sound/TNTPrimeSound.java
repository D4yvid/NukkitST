package org.crimsonmc.level.sound;

import org.crimsonmc.math.Vector3;
import org.crimsonmc.network.protocol.LevelEventPacket;

/**
 * Created by Pub4Game on 04.03.2016.
 */
public class TNTPrimeSound extends GenericSound {

    public TNTPrimeSound(Vector3 pos) {
        this(pos, 0);
    }

    public TNTPrimeSound(Vector3 pos, float pitch) {
        super(pos, LevelEventPacket.EVENT_SOUND_TNT, pitch);
    }
}
