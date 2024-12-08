package org.crimsonmc.level.particle;

import org.crimsonmc.block.BlockColor;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.network.protocol.DataPacket;
import org.crimsonmc.network.protocol.LevelEventPacket;

/**
 * Created on 2015/12/27 by xtypr. Package cn.crimsonmc.level.particle in project crimsonmc . The name
 * "spell" comes from minecraft wiki.
 */
public class SpellParticle extends Particle {

    protected final int data;

    public SpellParticle(Vector3 pos) {
        this(pos, 0);
    }

    public SpellParticle(Vector3 pos, int data) {
        super(pos.x, pos.y, pos.z);
        this.data = data;
    }

    public SpellParticle(Vector3 pos, BlockColor blockMapColor) {
        // alpha is ignored
        this(pos, blockMapColor.getRed(), blockMapColor.getGreen(), blockMapColor.getBlue());
    }

    public SpellParticle(Vector3 pos, int r, int g, int b) {
        this(pos, r, g, b, 0x00);
    }

    protected SpellParticle(Vector3 pos, int r, int g, int b, int a) {
        this(pos, ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff));
    }

    @Override
    public DataPacket[] encode() {
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_PARTICLE_SPLASH;
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.data = this.data;

        return new DataPacket[]{pk};
    }
}
