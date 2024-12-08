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
public class OpCommand extends VanillaCommand {

    public OpCommand(String name) {
        super(name, "%crimsonmc.command.op.description", "%commands.op.usage");
        this.setPermission("crimsonmc.command.op.give");
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

        String name = args[0];
        Player player = sender.getServer().getOfflinePlayer(name);

        Command.broadcastCommandMessage(
                sender, new TranslationContainer("commands.op.success", player.getName()));
        if (player instanceof ServerPlayer) {
            ((ServerPlayer) player).sendMessage(TextFormat.GRAY + "You are now op!");
        }

        player.setOp(true);

        return true;
    }
}
