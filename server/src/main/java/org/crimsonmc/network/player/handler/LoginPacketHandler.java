package org.crimsonmc.network.player.handler;

import org.crimsonmc.entity.data.StringEntityData;
import org.crimsonmc.event.player.PlayerPreLoginEvent;
import org.crimsonmc.exception.InvalidStateException;
import org.crimsonmc.network.binary.Binary;
import org.crimsonmc.network.player.PacketHandler;
import org.crimsonmc.network.protocol.LoginPacket;
import org.crimsonmc.network.protocol.PlayStatusPacket;
import org.crimsonmc.network.protocol.ProtocolInfo;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.text.TextFormat;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static org.crimsonmc.entity.Entity.DATA_NAMETAG;

public class LoginPacketHandler extends PacketHandler<LoginPacket> {

    private boolean checkUserName(@NotNull ServerPlayer player) {
        var pattern = Pattern.compile("[a-zA-Z0-9_]+");
        int len = player.getName().length();

        if (len > 16 || len < 3) {
            return false;
        }

        if (!pattern.matcher(player.getName()).matches()) {
            return false;
        }

        return !(player.getName().equalsIgnoreCase("rcon") &&
                player.getName().equalsIgnoreCase("console"));
    }

    @Override
    public boolean handlePacket(ServerPlayer player, LoginPacket packet) {
        player.getServer().getLogger().info(getClass().getName());

        if (player.loggedIn) {
            throw new InvalidStateException("The player is already logged-in");
        }

        if (packet.getProtocol() != ProtocolInfo.CURRENT_PROTOCOL) {
            String message;

            if (packet.getProtocol() < ProtocolInfo.CURRENT_PROTOCOL) {
                message = "disconnectionScreen.outdatedClient";

                PlayStatusPacket pk = new PlayStatusPacket();
                pk.status = PlayStatusPacket.LOGIN_FAILED_CLIENT;
                player.directDataPacket(pk);
            } else {
                message = "disconnectionScreen.outdatedServer";

                PlayStatusPacket pk = new PlayStatusPacket();
                pk.status = PlayStatusPacket.LOGIN_FAILED_SERVER;
                player.directDataPacket(pk);
            }
            player.close("", message, false);

            return false;
        }

        player.setUsername(TextFormat.clean(packet.username));
        player.setDisplayName(player.getUsername());
        player.setDataProperty(new StringEntityData(DATA_NAMETAG, player.getUsername()), false);

        if (player.getServer().getOnlinePlayers().size() >= player.getServer().getMaxPlayers() &&
                player.kick("disconnectionScreen.serverFull", false)) {
            return false;
        }

        player.setRandomClientId(packet.clientId);

        player.setUuid(packet.clientUUID);
        player.setRawUUID(Binary.writeUUID(player.getUuid()));

        if (!checkUserName(player)) {
            player.close("", "disconnectionScreen.invalidName");

            return false;
        }

        if (!packet.getSkin().isValid()) {
            player.close("", "disconnectionScreen.invalidSkin");

            return false;
        }

        player.setSkin(packet.getSkin());

        var playerPreLoginEvent = new PlayerPreLoginEvent(player, "Plugin reason");

        player.getServer().getPluginManager().callEvent(playerPreLoginEvent);

        if (playerPreLoginEvent.isCancelled()) {
            player.close("", playerPreLoginEvent.getKickMessage());

            return false;
        }

        player.onPlayerPreLogin();

        return true;
    }
}
