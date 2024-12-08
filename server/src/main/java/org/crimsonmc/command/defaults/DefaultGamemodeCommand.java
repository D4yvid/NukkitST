package org.crimsonmc.command.defaults;

import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.server.Server;

/**
 * Created on 2015/11/12 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class DefaultGamemodeCommand extends VanillaCommand {

    public DefaultGamemodeCommand(String name) {
        super(name, "%crimsonmc.command.defaultgamemode.description", "%commands.defaultgamemode.usage");
        this.setPermission("crimsonmc.command.defaultgamemode");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(
                    new TranslationContainer("commands.generic.usage", new String[]{this.usageMessage}));
            return false;
        }
        int gameMode = Server.getGamemodeFromString(args[0]);
        if (gameMode != -1) {
            sender.getServer().setPropertyInt("gamemode", gameMode);
            sender.sendMessage(new TranslationContainer(
                    "commands.defaultgamemode.success", new String[]{Server.getGamemodeString(gameMode)}));
        } else {
            sender.sendMessage("Unknown game mode"); //
        }
        return true;
    }
}
