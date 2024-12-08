package org.crimsonmc.level.sound;

import org.crimsonmc.math.Vector3;
import org.crimsonmc.network.protocol.LevelEventPacket;

/**
 * Created on 2015/11/21 by xtypr. Package cn.crimsonmc.level.sound in project crimsonmc .
 */
public class GhastSound extends GenericSound {

    public GhastSound(Vector3 pos) {
        this(pos, 0);
    }

    public GhastSound(Vector3 pos, float pitch) {
        super(pos, LevelEventPacket.EVENT_SOUND_GHAST, pitch);
    }
}