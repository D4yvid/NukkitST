package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.text.TextFormat;

/**
 * Created on 2015/11/11 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class KickCommand extends VanillaCommand {

    public KickCommand(String name) {
        super(name, "%crimsonmc.command.kick.description", "%commands.kick.usage");
        this.setPermission("crimsonmc.command.kick");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }

        String name = args[0];

        String reason = "";
        for (int i = 1; i < args.length; i++) {
            reason += args[i] + " ";
        }

        if (reason.length() > 0) {
            reason = reason.substring(0, reason.length() - 1);
        }

        ServerPlayer player = sender.getServer().getPlayer(name);
        if (player != null) {
            player.kick(reason);
            if (reason.length() >= 1) {
                Command.broadcastCommandMessage(
                        sender, new TranslationContainer("commands.kick.success.reason",
                                new String[]{player.getName(), reason}));
            } else {
                Command.broadcastCommandMessage(
                        sender, new TranslationContainer("commands.kick.success", player.getName()));
            }
        } else {
            sender.sendMessage(
                    new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
        }

        return true;
    }
}
