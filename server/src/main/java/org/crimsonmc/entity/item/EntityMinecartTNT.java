package org.crimsonmc.entity.item;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.entity.EntityExplosionPrimeEvent;
import org.crimsonmc.level.Explosion;
import org.crimsonmc.level.format.FullChunk;
import org.crimsonmc.nbt.tag.CompoundTag;

/**
 * Created by Snake1999 on 2016/1/30. Package cn.crimsonmc.entity.item in project crimsonmc.
 */
public class EntityMinecartTNT extends EntityMinecartEmpty {

    // TODO: 2016/1/30 NETWORK_ID

    public EntityMinecartTNT(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public boolean onUpdate(int currentTick) {
        Block downSide = this.getLocation().floor().getLevelBlock();
        if (downSide.getId() == Block.ACTIVATOR_RAIL && downSide.isPowered()) {
            explode();
            kill();
        }
        return true;
    }

    public void explode() {
        EntityExplosionPrimeEvent event = new EntityExplosionPrimeEvent(this, 4);
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        Explosion explosion = new Explosion(this, event.getForce(), this);
        if (event.isBlockBreaking()) {
            explosion.explodeA();
        }
        explosion.explodeB();
    }
}
