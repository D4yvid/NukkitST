package org.crimsonmc.command.defaults;

import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.text.TextFormat;

import java.util.Objects;

/**
 * Created on 2015/11/12 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class TellCommand extends VanillaCommand {

    public TellCommand(String name) {
        super(name, "%crimsonmc.command.tell.description", "%commands.message.usage",
                new String[]{"w", "msg"});
        this.setPermission("crimsonmc.command.tell");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));

            return false;
        }

        String name = args[0].toLowerCase();

        ServerPlayer player = sender.getServer().getPlayer(name);
        if (player == null) {
            sender.sendMessage(new TranslationContainer("commands.generic.player.notFound"));
            return true;
        }

        if (Objects.equals(player, sender)) {
            sender.sendMessage(new TranslationContainer(TextFormat.RED + ("%commands.message." +
                    "sameTarget")));
            return true;
        }

        String msg = "";
        for (int i = 1; i < args.length; i++) {
            msg += args[i] + " ";
        }
        if (msg.length() > 0) {
            msg = msg.substring(0, msg.length() - 1);
        }

        String displayName =
                (sender instanceof ServerPlayer ? ((ServerPlayer) sender).getDisplayName() : sender.getName());

        sender.sendMessage("[" + sender.getName() + " -> " + player.getDisplayName() + "] " + msg);
        player.sendMessage("[" + displayName + " -> " + player.getName() + "] " + msg);

        return true;
    }
}
