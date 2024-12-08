package org.crimsonmc.entity;

import org.crimsonmc.player.ServerPlayer;

/**
 * Author: BeYkeRYkt crimsonmc Project
 */
public interface EntityOwnable {

    String getOwnerName();

    void setOwnerName(String playerName);

    ServerPlayer getOwner();
}
