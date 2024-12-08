package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.level.Level;
import org.crimsonmc.level.Position;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.text.TextFormat;

import java.text.DecimalFormat;

/**
 * Created on 2015/12/13 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class SpawnpointCommand extends VanillaCommand {

    public SpawnpointCommand(String name) {
        super(name, "%crimsonmc.command.spawnpoint.description", "%commands.spawnpoint.usage");
        this.setPermission("crimsonmc.command.spawnpoint");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        ServerPlayer target;
        if (args.length == 0) {
            if (sender instanceof ServerPlayer) {
                target = (ServerPlayer) sender;
            } else {
                sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
                return true;
            }
        } else {
            target = sender.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(
                        new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
                return true;
            }
        }
        Level level = target.getLevel();
        DecimalFormat round2 = new DecimalFormat("##0.00");
        if (args.length == 4) {
            if (level != null) {
                int x;
                int y;
                int z;
                try {
                    x = Integer.parseInt(args[1]);
                    y = Integer.parseInt(args[2]);
                    z = Integer.parseInt(args[3]);
                } catch (NumberFormatException e1) {
                    sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return true;
                }
                if (y < 0)
                    y = 0;
                if (y > 128)
                    y = 128;
                target.setSpawn(new Position(x, y, z, level));
                Command.broadcastCommandMessage(
                        sender, new TranslationContainer("commands.spawnpoint.success",
                                new String[]{target.getName(), round2.format(x),
                                        round2.format(y), round2.format(z)}));
                return true;
            }
        } else if (args.length <= 1) {
            if (sender instanceof ServerPlayer) {
                Position pos = (Position) sender;
                target.setSpawn(pos);
                Command.broadcastCommandMessage(
                        sender,
                        new TranslationContainer("commands.spawnpoint.success",
                                new String[]{target.getName(), round2.format(pos.x),
                                        round2.format(pos.y), round2.format(pos.z)}));
                return true;
            } else {
                sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
                return true;
            }
        }
        sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
        return true;
    }
}
