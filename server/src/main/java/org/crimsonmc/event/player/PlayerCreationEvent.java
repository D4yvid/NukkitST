package org.crimsonmc.event.player;

import org.crimsonmc.event.Event;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.network.SourceInterface;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class PlayerCreationEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final SourceInterface interfaz;

    private final Long clientId;

    private final String address;

    private final int port;

    private Class<? extends ServerPlayer> baseClass;

    private Class<? extends ServerPlayer> playerClass;

    public PlayerCreationEvent(SourceInterface interfaz, Class<? extends ServerPlayer> baseClass,
                               Class<? extends ServerPlayer> playerClass, Long clientId, String address,
                               int port) {
        this.interfaz = interfaz;
        this.clientId = clientId;
        this.address = address;
        this.port = port;

        this.baseClass = baseClass;
        this.playerClass = playerClass;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public SourceInterface getInterface() {
        return interfaz;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public Long getClientId() {
        return clientId;
    }

    public Class<? extends ServerPlayer> getBaseClass() {
        return baseClass;
    }

    public void setBaseClass(Class<? extends ServerPlayer> baseClass) {
        this.baseClass = baseClass;
    }

    public Class<? extends ServerPlayer> getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(Class<? extends ServerPlayer> playerClass) {
        this.playerClass = playerClass;
    }
}
