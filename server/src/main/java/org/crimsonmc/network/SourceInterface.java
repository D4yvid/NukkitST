package org.crimsonmc.network;

import org.crimsonmc.network.protocol.DataPacket;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public interface SourceInterface {

    Integer putPacket(ServerPlayer player, DataPacket packet);

    Integer putPacket(ServerPlayer player, DataPacket packet, boolean needACK);

    Integer putPacket(ServerPlayer player, DataPacket packet, boolean needACK, boolean immediate);

    int getNetworkLatency(ServerPlayer player);

    void close(ServerPlayer player);

    void close(ServerPlayer player, String reason);

    void setName(String name);

    boolean process();

    void shutdown();

    void emergencyShutdown();
}
