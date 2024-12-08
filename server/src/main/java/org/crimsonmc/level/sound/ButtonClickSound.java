package org.crimsonmc.level.sound;

import org.crimsonmc.math.Vector3;
import org.crimsonmc.network.protocol.LevelEventPacket;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ButtonClickSound extends GenericSound {

    public ButtonClickSound(Vector3 pos) {
        this(pos, 0);
    }

    public ButtonClickSound(Vector3 pos, float pitch) {
        super(pos, LevelEventPacket.EVENT_SOUND_BUTTON_CLICK, pitch);
    }
}
