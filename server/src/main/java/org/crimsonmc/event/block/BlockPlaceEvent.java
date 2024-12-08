package org.crimsonmc.event.block;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.item.Item;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class BlockPlaceEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected final ServerPlayer player;

    protected final Item item;

    protected final Block blockReplace;

    protected final Block blockAgainst;

    public BlockPlaceEvent(ServerPlayer player, Block blockPlace, Block blockReplace, Block blockAgainst,
                           Item item) {
        super(blockPlace);
        this.blockReplace = blockReplace;
        this.blockAgainst = blockAgainst;
        this.item = item;
        this.player = player;
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

    public Block getBlockReplace() {
        return blockReplace;
    }

    public Block getBlockAgainst() {
        return blockAgainst;
    }
}
