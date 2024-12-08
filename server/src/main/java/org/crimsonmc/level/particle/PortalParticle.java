package org.crimsonmc.level.particle;

import org.crimsonmc.math.Vector3;

/**
 * Created on 2015/11/21 by xtypr. Package cn.crimsonmc.level.particle in project crimsonmc .
 */
public class PortalParticle extends GenericParticle {

    public PortalParticle(Vector3 pos) {
        super(pos, Particle.TYPE_PORTAL);
    }
}
