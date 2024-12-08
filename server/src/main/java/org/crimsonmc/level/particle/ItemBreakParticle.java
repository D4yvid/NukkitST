package org.crimsonmc.level.particle;

import org.crimsonmc.item.Item;
import org.crimsonmc.math.Vector3;

/**
 * Created on 2015/11/21 by xtypr. Package cn.crimsonmc.level.particle in project crimsonmc .
 */
public class ItemBreakParticle extends GenericParticle {

    public ItemBreakParticle(Vector3 pos, Item item) {
        super(pos, Particle.TYPE_ITEM_BREAK, (item.getId() << 16) | item.getDamage());
    }
}
