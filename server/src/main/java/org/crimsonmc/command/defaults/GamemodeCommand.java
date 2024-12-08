package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.server.Server;
import org.crimsonmc.text.TextFormat;

/**
 * Created on 2015/11/13 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class GamemodeCommand extends VanillaCommand {

    public GamemodeCommand(String name) {
        super(name, "%crimsonmc.command.gamemode.description", "%commands.gamemode.usage");
        this.setPermission("crimsonmc.command.gamemode");
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

        int gameMode = Server.getGamemodeFromString(args[0]);

        if (gameMode == -1) {
            sender.sendMessage("Unknown game mode");

            return true;
        }

        CommandSender target = sender;

        if (args.length > 1) {
            target = sender.getServer().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(
                        new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));

                return true;
            }
        } else if (!(sender instanceof ServerPlayer)) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));

            return true;
        }

        ((ServerPlayer) target).setGamemode(gameMode);

        if (gameMode != ((ServerPlayer) target).getGamemode()) {
            sender.sendMessage("Game mode update for " + target.getName() + " failed");
        } else {
            if (target.equals(sender)) {
                Command.broadcastCommandMessage(
                        sender, new TranslationContainer("commands.gamemode.success.self",
                                Server.getGamemodeString(gameMode)));
            } else {
                target.sendMessage(new TranslationContainer("gameMode.changed"));
                Command.broadcastCommandMessage(
                        sender, new TranslationContainer(
                                "commands.gamemode.success.other",
                                new String[]{target.getName(), Server.getGamemodeString(gameMode)}));
            }
        }

        return true;
    }
}
