package org.crimsonmc.level.particle;

import org.crimsonmc.block.Block;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.network.protocol.DataPacket;
import org.crimsonmc.network.protocol.LevelEventPacket;

/**
 * Created on 2015/11/21 by xtypr. Package cn.crimsonmc.level.particle in project crimsonmc .
 */
public class DestroyBlockParticle extends Particle {

    protected final int data;

    public DestroyBlockParticle(Vector3 pos, Block block) {
        super(pos.x, pos.y, pos.z);
        this.data = block.getId() + (block.getDamage() << 12);
    }

    @Override
    public DataPacket[] encode() {
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_PARTICLE_DESTROY;
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.data = this.data;

        return new DataPacket[]{pk};
    }
}
