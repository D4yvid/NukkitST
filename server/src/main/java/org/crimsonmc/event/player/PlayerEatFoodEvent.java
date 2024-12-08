package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.item.food.Food;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created by Snake1999 on 2016/1/14. Package cn.crimsonmc.event.player in project crimsonmc.
 */
public class PlayerEatFoodEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Food food;

    public PlayerEatFoodEvent(ServerPlayer player, Food food) {
        this.player = player;
        this.food = food;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
