package org.crimsonmc.network.protocol;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class SetPlayerGameTypePacket extends DataPacket {

    public final static byte NETWORK_ID = ProtocolInfo.SET_PLAYER_GAMETYPE_PACKET;

    public int gamemode;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putInt(this.gamemode);
    }
}
