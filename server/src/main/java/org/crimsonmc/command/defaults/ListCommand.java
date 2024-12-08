package org.crimsonmc.command.defaults;

import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created on 2015/11/11 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class ListCommand extends VanillaCommand {

    public ListCommand(String name) {
        super(name, "%crimsonmc.command.list.description", "%commands.players.usage");
        this.setPermission("crimsonmc.command.list");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        String online = "";
        int onlineCount = 0;
        for (ServerPlayer player : sender.getServer().getOnlinePlayers().values()) {
            if (player.isOnline() && (!(sender instanceof ServerPlayer) || ((ServerPlayer) sender).canSee(player))) {
                online += player.getDisplayName() + ", ";
                ++onlineCount;
            }
        }

        if (online.length() > 0) {
            online = online.substring(0, online.length() - 2);
        }

        sender.sendMessage(new TranslationContainer(
                "commands.players.list",
                new String[]{String.valueOf(onlineCount),
                        String.valueOf(sender.getServer().getMaxPlayers())}));
        sender.sendMessage(online);
        return true;
    }
}
