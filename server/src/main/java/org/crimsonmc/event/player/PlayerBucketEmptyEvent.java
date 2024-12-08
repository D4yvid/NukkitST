package org.crimsonmc.event.player;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.item.Item;
import org.crimsonmc.player.ServerPlayer;

public class PlayerBucketEmptyEvent extends PlayerBucketEvent {

    private static final HandlerList handlers = new HandlerList();

    public PlayerBucketEmptyEvent(ServerPlayer who, Block blockClicked, Integer blockFace, Item bucket,
                                  Item itemInHand) {
        super(who, blockClicked, blockFace, bucket, itemInHand);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
