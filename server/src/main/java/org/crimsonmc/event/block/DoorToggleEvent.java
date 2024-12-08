package org.crimsonmc.event.block;

import org.crimsonmc.block.Block;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created by Snake1999 on 2016/1/22. Package cn.crimsonmc.event.block in project crimsonmc.
 */
public class DoorToggleEvent extends BlockUpdateEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private ServerPlayer player;

    public DoorToggleEvent(Block block, ServerPlayer player) {
        super(block);
        this.player = player;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ServerPlayer player) {
        this.player = player;
    }
}
