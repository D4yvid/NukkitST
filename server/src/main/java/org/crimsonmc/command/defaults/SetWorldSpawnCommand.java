package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.level.Level;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.player.ServerPlayer;

import java.text.DecimalFormat;

/**
 * Created on 2015/12/13 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class SetWorldSpawnCommand extends VanillaCommand {

    public SetWorldSpawnCommand(String name) {
        super(name, "%crimsonmc.command.setworldspawn.description", "%commands.setworldspawn.usage");
        this.setPermission("crimsonmc.command.setworldspawn");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        Level level;
        Vector3 pos;
        if (args.length == 0) {
            if (sender instanceof ServerPlayer) {
                level = ((ServerPlayer) sender).getLevel();
                pos = ((ServerPlayer) sender).round();
            } else {
                sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
                return true;
            }
        } else if (args.length == 3) {
            level = sender.getServer().getDefaultLevel();
            try {
                pos = new Vector3(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]));
            } catch (NumberFormatException e1) {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
        } else {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        level.setSpawnLocation(pos);
        DecimalFormat round2 = new DecimalFormat("##0.00");
        Command.broadcastCommandMessage(
                sender, new TranslationContainer("commands.setworldspawn.success",
                        new String[]{round2.format(pos.x), round2.format(pos.y),
                                round2.format(pos.z)}));
        return true;
    }
}
