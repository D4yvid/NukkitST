package org.crimsonmc.event.block;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.item.Item;
import org.crimsonmc.player.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockBreakEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected final ServerPlayer player;

    protected final Item item;

    protected boolean instaBreak = false;

    protected Item[] blockDrops = new Item[0];

    protected boolean fastBreak = false;

    public BlockBreakEvent(ServerPlayer player, Block block, Item item) {
        this(player, block, item, false, false);
    }

    public BlockBreakEvent(ServerPlayer player, Block block, Item item, boolean instaBreak) {
        this(player, block, item, instaBreak, false);
    }

    public BlockBreakEvent(ServerPlayer player, Block block, Item item, boolean instaBreak,
                           boolean fastBreak) {
        super(block);
        this.item = item;
        this.player = player;
        this.instaBreak = instaBreak;
        int[][] drops = player.isSurvival() ? block.getDrops(item) : new int[0][0];
        List<Item> list = new ArrayList<>();
        for (int[] i : drops) {
            list.add(Item.get(i[0], i[1], i[2]));
        }
        this.blockDrops = list.toArray(new Item[list.size()]);
        this.fastBreak = fastBreak;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public Item getItem() {
        return item;
    }

    public boolean getInstaBreak() {
        return this.instaBreak;
    }

    public void setInstaBreak(boolean instaBreak) {
        this.instaBreak = instaBreak;
    }

    public Item[] getDrops() {
        return blockDrops;
    }

    public void setDrops(Item[] drops) {
        this.blockDrops = drops;
    }

    public boolean isFastBreak() {
        return this.fastBreak;
    }
}
