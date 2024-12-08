package org.crimsonmc.block;

import org.crimsonmc.event.block.BlockGrowEvent;
import org.crimsonmc.item.Item;
import org.crimsonmc.level.Level;
import org.crimsonmc.math.RandomUtilities;
import org.crimsonmc.server.Server;

/**
 * Created by Pub4Game on 15.01.2016.
 */
public class BlockStemPumpkin extends BlockCrops {

    public BlockStemPumpkin() {
        this(0);
    }

    public BlockStemPumpkin(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return PUMPKIN_STEM;
    }

    @Override
    public String getName() {
        return "Pumpkin Stem";
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.getSide(0).isTransparent()) {
                this.getLevel().useBreakOn(this);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            RandomUtilities random = new RandomUtilities();
            if (random.nextRange(1, 2) == 1) {
                if (this.meta < 0x07) {
                    Block block = this.clone();
                    ++block.meta;
                    BlockGrowEvent ev = new BlockGrowEvent(this, block);
                    Server.getInstance().getPluginManager().callEvent(ev);
                    if (!ev.isCancelled()) {
                        this.getLevel().setBlock(this, ev.getNewState(), true);
                    }
                    return Level.BLOCK_UPDATE_RANDOM;
                } else {
                    for (int side = 2; side <= 5; ++side) {
                        Block b = this.getSide(side);
                        if (b.getId() == PUMPKIN) {
                            return Level.BLOCK_UPDATE_RANDOM;
                        }
                    }
                    Block side = this.getSide(random.nextRange(2, 5));
                    Block d = side.getSide(0);
                    if (side.getId() == AIR &&
                            (d.getId() == FARMLAND || d.getId() == GRASS || d.getId() == DIRT)) {
                        BlockGrowEvent ev = new BlockGrowEvent(side, new BlockPumpkin());
                        Server.getInstance().getPluginManager().callEvent(ev);
                        if (!ev.isCancelled()) {
                            this.getLevel().setBlock(side, ev.getNewState(), true);
                        }
                    }
                }
            }
            return Level.BLOCK_UPDATE_RANDOM;
        }
        return 0;
    }

    @Override
    public int[][] getDrops(Item item) {
        RandomUtilities random = new RandomUtilities();
        return new int[][]{{Item.PUMPKIN_SEEDS, 0, random.nextRange(0, 2)}};
    }
}
