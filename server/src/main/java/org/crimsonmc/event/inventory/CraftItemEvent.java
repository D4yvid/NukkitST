package org.crimsonmc.event.inventory;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.Event;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.inventory.Recipe;
import org.crimsonmc.item.Item;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class CraftItemEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Recipe recipe;

    private final ServerPlayer player;

    private Item[] input = new Item[0];

    public CraftItemEvent(ServerPlayer player, Item[] input, Recipe recipe) {
        this.player = player;
        this.input = input;
        this.recipe = recipe;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Item[] getInput() {
        Item[] items = new Item[this.input.length];
        for (int i = 0; i < this.input.length; i++) {
            items[i] = this.input[i].clone();
        }

        return items;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public ServerPlayer getPlayer() {
        return player;
    }
}