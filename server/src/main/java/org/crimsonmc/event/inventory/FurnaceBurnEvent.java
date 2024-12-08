package org.crimsonmc.event.inventory;

import org.crimsonmc.blockentity.BlockEntityFurnace;
import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.event.block.BlockEvent;
import org.crimsonmc.item.Item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class FurnaceBurnEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final BlockEntityFurnace furnace;

    private final Item fuel;

    private short burnTime;

    private boolean burning = true;

    public FurnaceBurnEvent(BlockEntityFurnace furnace, Item fuel, short burnTime) {
        super(furnace.getBlock());
        this.fuel = fuel;
        this.burnTime = burnTime;
        this.furnace = furnace;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public BlockEntityFurnace getFurnace() {
        return furnace;
    }

    public Item getFuel() {
        return fuel;
    }

    public short getBurnTime() {
        return burnTime;
    }

    public void setBurnTime(short burnTime) {
        this.burnTime = burnTime;
    }

    public boolean isBurning() {
        return burning;
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }
}