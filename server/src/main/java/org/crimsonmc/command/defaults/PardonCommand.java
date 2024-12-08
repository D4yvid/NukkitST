package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class PardonCommand extends VanillaCommand {

    public PardonCommand(String name) {
        super(name, "%crimsonmc.command.unban.player.description", "%commands.unban.usage");
        this.setPermission("crimsonmc.command.unban.player");
        this.setAliases(new String[]{"unban"});
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));

            return false;
        }

        sender.getServer().getNameBans().remove(args[0]);

        Command.broadcastCommandMessage(sender,
                new TranslationContainer("%commands.unban.success", args[0]));

        return true;
    }
}
