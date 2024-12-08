package org.crimsonmc.inventory;

import org.crimsonmc.blockentity.BlockEntityFurnace;
import org.crimsonmc.item.Item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class FurnaceInventory extends ContainerInventory {

    public FurnaceInventory(BlockEntityFurnace furnace) {
        super(furnace, InventoryType.FURNACE);
    }

    @Override
    public BlockEntityFurnace getHolder() {
        return (BlockEntityFurnace) this.holder;
    }

    public Item getResult() {
        return this.getItem(2);
    }

    public Item getFuel() {
        return this.getItem(1);
    }

    public Item getSmelting() {
        return this.getItem(0);
    }

    public boolean setResult(Item item) {
        return this.setItem(2, item);
    }

    public boolean setFuel(Item item) {
        return this.setItem(1, item);
    }

    public boolean setSmelting(Item item) {
        return this.setItem(0, item);
    }

    @Override
    public void onSlotChange(int index, Item before) {
        super.onSlotChange(index, before);

        this.getHolder().scheduleUpdate();
    }
}
