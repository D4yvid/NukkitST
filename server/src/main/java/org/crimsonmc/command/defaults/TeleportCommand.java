package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.event.player.PlayerTeleportEvent;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.level.Location;
import org.crimsonmc.math.MathUtilities;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.text.TextFormat;

/**
 * Created on 2015/11/12 by Pub4Game and milkice. Package cn.crimsonmc.command.defaults in project
 * crimsonmc .
 */
public class TeleportCommand extends VanillaCommand {

    public TeleportCommand(String name) {
        super(name, "%crimsonmc.command.tp.description", "%commands.tp.usage");
        this.setPermission("crimsonmc.command.teleport");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length < 1 || args.length > 6) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        CommandSender target;
        CommandSender origin = sender;
        if (args.length == 1 || args.length == 3) {
            if (sender instanceof ServerPlayer) {
                target = sender;
            } else {
                sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
                return true;
            }
            if (args.length == 1) {
                target = sender.getServer().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(TextFormat.RED + "Can't find player " + args[0]);
                    return true;
                }
            }
        } else {
            target = sender.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(TextFormat.RED + "Can't find player " + args[0]);
                return true;
            }
            if (args.length == 2) {
                origin = target;
                target = sender.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(TextFormat.RED + "Can't find player " + args[1]);
                    return true;
                }
            }
        }
        if (args.length < 3) {
            ((ServerPlayer) origin).teleport((ServerPlayer) target, PlayerTeleportEvent.TeleportCause.COMMAND);
            Command.broadcastCommandMessage(
                    sender, new TranslationContainer("commands.tp.success",
                            new String[]{origin.getName(), target.getName()}));
            return true;
        } else if (((ServerPlayer) target).getLevel() != null) {
            int pos;
            if (args.length == 4 || args.length == 6) {
                pos = 1;
            } else {
                pos = 0;
            }
            double x;
            double y;
            double z;
            double yaw;
            double pitch;
            try {
                x = Double.parseDouble(args[pos++]);
                y = Double.parseDouble(args[pos++]);
                z = Double.parseDouble(args[pos++]);
                yaw = ((ServerPlayer) target).getYaw();
                pitch = ((ServerPlayer) target).getPitch();
            } catch (NumberFormatException e1) {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
            if (y < 0)
                y = 0;
            if (y > 128)
                y = 128;
            if (args.length == 6 || (args.length == 5 && pos == 3)) {
                yaw = Integer.parseInt(args[pos++]);
                pitch = Integer.parseInt(args[pos++]);
            }
            ((ServerPlayer) target)
                    .teleport(new Location(x, y, z, yaw, pitch, ((ServerPlayer) target).getLevel()),
                            PlayerTeleportEvent.TeleportCause.COMMAND);
            Command.broadcastCommandMessage(
                    sender, new TranslationContainer("commands.tp.success.coordinates",
                            new String[]{target.getName(),
                                    String.valueOf(MathUtilities.round(x, 2)),
                                    String.valueOf(MathUtilities.round(y, 2)),
                                    String.valueOf(MathUtilities.round(z, 2))}));
            return true;
        }
        sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
        return true;
    }
}
