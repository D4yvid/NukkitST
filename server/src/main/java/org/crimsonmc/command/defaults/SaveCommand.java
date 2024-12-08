package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.level.Level;
import org.crimsonmc.player.ServerPlayer;

/**
 * Created on 2015/11/13 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class SaveCommand extends VanillaCommand {

    public SaveCommand(String name) {
        super(name, "%crimsonmc.command.save.description", "%commands.save.usage");
        this.setPermission("crimsonmc.command.save.perform");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.save.start"));

        for (ServerPlayer player : sender.getServer().getOnlinePlayers().values()) {
            player.save();
        }

        for (Level level : sender.getServer().getLevels().values()) {
            level.save(true);
        }

        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.save.success"));
        return true;
    }
}
