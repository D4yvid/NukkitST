package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;

/**
 * Created on 2015/11/13 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class SaveOnCommand extends VanillaCommand {

    public SaveOnCommand(String name) {
        super(name, "%crimsonmc.command.saveon.description", "%commands.save-on.usage");
        this.setPermission("crimsonmc.command.save.enable");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        sender.getServer().setAutoSave(true);
        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.save.enabled"));
        return true;
    }
}
