package org.crimsonmc.inventory;

import org.crimsonmc.blockentity.BlockEntityBrewingStand;
import org.crimsonmc.item.Item;

public class BrewingInventory extends ContainerInventory {

    public BrewingInventory(BlockEntityBrewingStand brewingStand) {
        super(brewingStand, InventoryType.BREWING_STAND);
    }

    @Override
    public BlockEntityBrewingStand getHolder() {
        return (BlockEntityBrewingStand) this.holder;
    }

    public Item getIngredient() {
        return getItem(0);
    }

    public void setIngredient(Item item) {
        setItem(0, item);
    }

    @Override
    public void onSlotChange(int index, Item before) {
        super.onSlotChange(index, before);

        this.getHolder().scheduleUpdate();
    }
}