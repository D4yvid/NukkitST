package org.crimsonmc.block;

import org.crimsonmc.entity.Entity;
import org.crimsonmc.event.entity.EntityPortalEnterEvent;
import org.crimsonmc.item.Item;

/**
 * Created on 2016/1/5 by xtypr. Package cn.crimsonmc.block in project crimsonmc . The name
 * NetherPortalBlock comes from minecraft wiki.
 */
public class BlockNetherPortal extends BlockFlowable {

    public BlockNetherPortal() {
        this(0);
    }

    public BlockNetherPortal(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "Nether Portal Block";
    }

    @Override
    public int getId() {
        return NETHER_PORTAL;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public int getLightLevel() {
        return 11;
    }

    @Override
    public boolean onBreak(Item item) {
        boolean result = super.onBreak(item);
        for (int side = 0; side <= 5; side++) {
            Block b = this.getSide(side);
            if (b != null) {
                if (b instanceof BlockNetherPortal) {
                    result &= b.onBreak(item);
                }
            }
        }
        return result;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.inPortalTicks++;

        if (entity.inPortalTicks >= 80) {
            EntityPortalEnterEvent ev =
                    new EntityPortalEnterEvent(entity, EntityPortalEnterEvent.TYPE_NETHER);
            this.level.getServer().getPluginManager().callEvent(ev);

            if (ev.isCancelled()) {
            }

            // todo: teleport to the nether
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}
