package org.crimsonmc.level.sound;

import org.crimsonmc.math.Vector3;
import org.crimsonmc.network.protocol.LevelEventPacket;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class LeverSound extends GenericSound {

    public LeverSound(Vector3 pos, boolean isPowerOn) {
        super(pos, LevelEventPacket.EVENT_SOUND_BUTTON_CLICK, isPowerOn ? 0.7f : 0.5f);
    }
}
