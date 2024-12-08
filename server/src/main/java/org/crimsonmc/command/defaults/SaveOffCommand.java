package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;

/**
 * Created on 2015/11/13 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class SaveOffCommand extends VanillaCommand {

    public SaveOffCommand(String name) {
        super(name, "%crimsonmc.command.saveoff.description", "%commands.save-off.usage");
        this.setPermission("crimsonmc.command.save.disable");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        sender.getServer().setAutoSave(false);
        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.save.disabled"));
        return true;
    }
}
