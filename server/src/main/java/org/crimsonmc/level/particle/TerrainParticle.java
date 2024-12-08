package org.crimsonmc.level.particle;

import org.crimsonmc.block.Block;
import org.crimsonmc.math.Vector3;

/**
 * Created on 2015/11/21 by xtypr. Package cn.crimsonmc.level.particle in project crimsonmc .
 */
public class TerrainParticle extends GenericParticle {

    public TerrainParticle(Vector3 pos, Block block) {
        super(pos, Particle.TYPE_TERRAIN, (block.getDamage() << 8) | block.getId());
    }
}
