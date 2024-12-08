package org.crimsonmc.command.defaults;

import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.text.TextFormat;

/**
 * Created on 2015/11/12 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class MeCommand extends VanillaCommand {

    public MeCommand(String name) {
        super(name, "%crimsonmc.command.me.description", "%commands.me.usage");
        this.setPermission("crimsonmc.command.me");
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

        String name;
        if (sender instanceof ServerPlayer) {
            name = ((ServerPlayer) sender).getDisplayName();
        } else {
            name = sender.getName();
        }

        String msg = "";
        for (String arg : args) {
            msg += arg + " ";
        }

        if (msg.length() > 0) {
            msg = msg.substring(0, msg.length() - 1);
        }

        sender.getServer().broadcastMessage(
                new TranslationContainer("chat.type.emote", new String[]{name, TextFormat.WHITE + msg}));

        return true;
    }
}
