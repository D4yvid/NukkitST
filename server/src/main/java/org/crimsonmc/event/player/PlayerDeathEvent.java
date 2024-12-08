package org.crimsonmc.event.player;

import org.crimsonmc.event.Cancellable;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.event.entity.EntityDeathEvent;
import org.crimsonmc.item.Item;
import org.crimsonmc.lang.TextContainer;
import org.crimsonmc.player.ServerPlayer;

public class PlayerDeathEvent extends EntityDeathEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private TextContainer deathMessage;

    private boolean keepInventory = false;

    private boolean keepExperience = false;

    private int experience;

    public PlayerDeathEvent(ServerPlayer player, Item[] drops, TextContainer deathMessage, int experience) {
        super(player, drops);
        this.deathMessage = deathMessage;
        this.experience = experience;
    }

    public PlayerDeathEvent(ServerPlayer player, Item[] drops, String deathMessage, int experience) {
        this(player, drops, new TextContainer(deathMessage), experience);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public ServerPlayer getEntity() {
        return (ServerPlayer) super.getEntity();
    }

    public TextContainer getDeathMessage() {
        return deathMessage;
    }

    public void setDeathMessage(TextContainer deathMessage) {
        this.deathMessage = deathMessage;
    }

    public void setDeathMessage(String deathMessage) {
        this.deathMessage = new TextContainer(deathMessage);
    }

    public boolean getKeepInventory() {
        return keepInventory;
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    public boolean getKeepExperience() {
        return keepExperience;
    }

    public void setKeepExperience(boolean keepExperience) {
        this.keepExperience = keepExperience;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
