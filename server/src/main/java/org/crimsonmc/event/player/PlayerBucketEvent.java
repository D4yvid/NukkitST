package org.crimsonmc.event.player;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.item.Item;
import org.crimsonmc.player.ServerPlayer;

abstract class PlayerBucketEvent extends PlayerEvent implements Cancellable {

    private final Block blockClicked;

    private final Integer blockFace;

    private final Item bucket;

    private Item item;

    public PlayerBucketEvent(ServerPlayer who, Block blockClicked, Integer blockFace, Item bucket,
                             Item itemInHand) {
        this.player = who;
        this.blockClicked = blockClicked;
        this.blockFace = blockFace;
        this.item = itemInHand;
        this.bucket = bucket;
    }

    /**
     * Returns the bucket used in this event
     */
    public Item getBucket() {
        return this.bucket;
    }

    /**
     * Returns the item in hand after the event
     */
    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Block getBlockClicked() {
        return this.blockClicked;
    }

    public int getBlockFace() {
        return this.blockFace;
    }
}
