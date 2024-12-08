package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.player.Player;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.text.TextFormat;

/**
 * Created on 2015/11/12 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class DeopCommand extends VanillaCommand {

    public DeopCommand(String name) {
        super(name, "%crimsonmc.command.deop.description", "%commands.deop.usage");
        this.setPermission("crimsonmc.command.op.take");
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

        String playerName = args[0];
        Player player = sender.getServer().getOfflinePlayer(playerName);
        player.setOp(false);

        if (player instanceof ServerPlayer) {
            ((ServerPlayer) player).sendMessage(TextFormat.GRAY + "You are no longer op!");
        }

        Command.broadcastCommandMessage(
                sender, new TranslationContainer("commands.deop.success", new String[]{player.getName()}));

        return true;
    }
}
