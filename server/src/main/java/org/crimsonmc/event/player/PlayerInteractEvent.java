package org.crimsonmc.event.player;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.item.Item;
import org.crimsonmc.level.Position;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class PlayerInteractEvent extends PlayerEvent implements Cancellable {

    public static final int LEFT_CLICK_BLOCK = 0;

    public static final int RIGHT_CLICK_BLOCK = 1;

    public static final int LEFT_CLICK_AIR = 2;

    public static final int RIGHT_CLICK_AIR = 3;

    public static final int PHYSICAL = 4;

    private static final HandlerList handlers = new HandlerList();

    protected final Block blockTouched;

    protected final Vector3 touchVector;

    protected final int blockFace;

    protected final Item item;

    protected final int action;

    public PlayerInteractEvent(ServerPlayer player, Item item, Vector3 block, int face) {
        this(player, item, block, face, RIGHT_CLICK_BLOCK);
    }

    public PlayerInteractEvent(ServerPlayer player, Item item, Vector3 block, int face, int action) {
        if (block instanceof Block) {
            this.blockTouched = (Block) block;
            this.touchVector = new Vector3(0, 0, 0);
        } else {
            this.touchVector = block;
            this.blockTouched = Block.get(Block.AIR, 0, new Position(0, 0, 0, player.level));
        }

        this.player = player;
        this.item = item;
        this.blockFace = face;
        this.action = action;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public int getAction() {
        return action;
    }

    public Item getItem() {
        return item;
    }

    public Block getBlock() {
        return blockTouched;
    }

    public Vector3 getTouchVector() {
        return touchVector;
    }

    public int getFace() {
        return blockFace;
    }
}
