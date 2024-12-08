package org.crimsonmc.level.particle;

import org.crimsonmc.math.Vector3;

/**
 * Created on 2015/11/21 by xtypr. Package cn.crimsonmc.level.particle in project crimsonmc .
 */
public class RedstoneParticle extends GenericParticle {

    public RedstoneParticle(Vector3 pos) {
        this(pos, 1);
    }

    public RedstoneParticle(Vector3 pos, int lifetime) {
        super(pos, Particle.TYPE_REDSTONE, lifetime);
    }
}
