package org.crimsonmc.level.particle;

import org.crimsonmc.math.Vector3;

/**
 * Created on 2015/11/21 by xtypr. Package cn.crimsonmc.level.particle in project crimsonmc .
 */
public class InstantEnchantParticle extends GenericParticle {

    public InstantEnchantParticle(Vector3 pos) {
        super(pos, Particle.TYPE_MOB_SPELL_INSTANTANEOUS);
    }
}
