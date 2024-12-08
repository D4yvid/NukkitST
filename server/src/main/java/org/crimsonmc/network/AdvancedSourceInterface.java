package org.crimsonmc.network;

/**
 * author: MagicDroidX crimsonmc Project
 */
public interface AdvancedSourceInterface extends SourceInterface {

    void blockAddress(String address);

    void blockAddress(String address, int timeout);

    void setNetwork(Network network);

    void sendRawPacket(String address, int port, byte[] payload);
}
