package org.crimsonmc.command.defaults;

import org.crimsonmc.command.CommandSender;
import org.crimsonmc.command.ConsoleCommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.text.TextFormat;

/**
 * Created on 2015/11/12 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class SayCommand extends VanillaCommand {

    public SayCommand(String name) {
        super(name, "%crimsonmc.command.say.description", "%commands.say.usage");
        this.setPermission("crimsonmc.command.say");
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

        String senderString;
        if (sender instanceof ServerPlayer) {
            senderString = ((ServerPlayer) sender).getDisplayName();
        } else if (sender instanceof ConsoleCommandSender) {
            senderString = "Server";
        } else {
            senderString = sender.getName();
        }

        String msg = "";
        for (String arg : args) {
            msg += arg + " ";
        }
        if (msg.length() > 0) {
            msg = msg.substring(0, msg.length() - 1);
        }

        sender.getServer().broadcastMessage(
                new TranslationContainer(TextFormat.LIGHT_PURPLE + "%chat.type.announcement",
                        new String[]{senderString, TextFormat.LIGHT_PURPLE + msg}));
        return true;
    }
}
