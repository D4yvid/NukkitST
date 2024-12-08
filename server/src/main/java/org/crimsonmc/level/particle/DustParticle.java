package org.crimsonmc.level.particle;

import org.crimsonmc.block.BlockColor;
import org.crimsonmc.math.Vector3;

/**
 * Created on 2015/11/21 by xtypr. Package cn.crimsonmc.level.particle in project crimsonmc .
 */
public class DustParticle extends GenericParticle {

    public DustParticle(Vector3 pos, BlockColor blockMapColor) {
        this(pos, blockMapColor.getRed(), blockMapColor.getGreen(), blockMapColor.getBlue(),
                blockMapColor.getAlpha());
    }

    public DustParticle(Vector3 pos, int r, int g, int b) {
        this(pos, r, g, b, 255);
    }

    public DustParticle(Vector3 pos, int r, int g, int b, int a) {
        super(pos, Particle.TYPE_DUST,
                ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff));
    }
}
