package org.crimsonmc.event.block;

import org.crimsonmc.block.Block;
import org.crimsonmc.blockentity.BlockEntityItemFrame;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.item.Item;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created by Pub4Game on 03.07.2016.
 */
public class ItemFrameDropItemEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final ServerPlayer player;

    private final Item item;

    private final BlockEntityItemFrame itemFrame;

    public ItemFrameDropItemEvent(ServerPlayer player, Block block, BlockEntityItemFrame itemFrame,
                                  Item item) {
        super(block);
        this.player = player;
        this.itemFrame = itemFrame;
        this.item = item;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public BlockEntityItemFrame getItemFrame() {
        return itemFrame;
    }

    public Item getItem() {
        return item;
    }
}