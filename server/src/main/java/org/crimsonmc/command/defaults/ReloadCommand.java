package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.text.TextFormat;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ReloadCommand extends VanillaCommand {

    public ReloadCommand(String name) {
        super(name, "%crimsonmc.command.reload.description", "%commands.reload.usage");
        this.setPermission("crimsonmc.command.reload");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        Command.broadcastCommandMessage(
                sender, new TranslationContainer(TextFormat.YELLOW + "%crimsonmc.command.reload.reloading" +
                        TextFormat.WHITE));

        sender.getServer().reload();

        Command.broadcastCommandMessage(
                sender, new TranslationContainer(TextFormat.YELLOW + "%crimsonmc.command.reload.reloaded" +
                        TextFormat.WHITE));

        return true;
    }
}
