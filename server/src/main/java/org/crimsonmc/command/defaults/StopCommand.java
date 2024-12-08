package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class StopCommand extends VanillaCommand {

    public StopCommand(String name) {
        super(name, "%crimsonmc.command.stop.description", "%commands.stop.usage");
        this.setPermission("crimsonmc.command.stop");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.stop.start"));

        sender.getServer().shutdown();

        return true;
    }
}
