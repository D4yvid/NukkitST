package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;

/**
 * Created on 2015/11/21 by xtypr. Package cn.nukkit.level.sound in project Nukkit .
 */
public class AnvilFallSound extends GenericSound {

  public AnvilFallSound(Vector3 pos) { this(pos, 0); }

  public AnvilFallSound(Vector3 pos, float pitch) {
    super(pos, LevelEventPacket.EVENT_SOUND_ANVIL_FALL, pitch);
  }
}
