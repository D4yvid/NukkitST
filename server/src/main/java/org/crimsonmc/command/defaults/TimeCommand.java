package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.level.Level;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.text.TextFormat;

/**
 * Created on 2015/11/11 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class TimeCommand extends VanillaCommand {

    public TimeCommand(String name) {
        super(name, "%crimsonmc.command.time.description", "%crimsonmc.command.time.usage");
        this.setPermission("crimsonmc.command.time.add;"
                + "crimsonmc.command.time.set;"
                + "crimsonmc.command.time.start;"
                + "crimsonmc.command.time.stop");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));

            return false;
        }

        if ("start".equals(args[0])) {
            if (!sender.hasPermission("crimsonmc.command.time.start")) {
                sender.sendMessage(
                        new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));

                return true;
            }
            for (Level level : sender.getServer().getLevels().values()) {
                level.checkTime();
                level.startTime();
                level.checkTime();
            }
            Command.broadcastCommandMessage(sender, "Restarted the time");
            return true;
        } else if ("stop".equals(args[0])) {
            if (!sender.hasPermission("crimsonmc.command.time.stop")) {
                sender.sendMessage(
                        new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));

                return true;
            }
            for (Level level : sender.getServer().getLevels().values()) {
                level.checkTime();
                level.stopTime();
                level.checkTime();
            }
            Command.broadcastCommandMessage(sender, "Stopped the time");
            return true;
        } else if ("query".equals(args[0])) {
            if (!sender.hasPermission("crimsonmc.command.time.query")) {
                sender.sendMessage(
                        new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));

                return true;
            }
            Level level;
            if (sender instanceof ServerPlayer) {
                level = ((ServerPlayer) sender).getLevel();
            } else {
                level = sender.getServer().getDefaultLevel();
            }
            sender.sendMessage(
                    new TranslationContainer("commands.time.query", String.valueOf(level.getTime())));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));

            return false;
        }

        if ("set".equals(args[0])) {
            if (!sender.hasPermission("crimsonmc.command.time.set")) {
                sender.sendMessage(
                        new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));

                return true;
            }

            int value;
            if ("day".equals(args[1])) {
                value = Level.TIME_DAY;
            } else if ("night".equals(args[1])) {
                value = Level.TIME_NIGHT;
            } else {
                try {
                    value = Math.max(0, Integer.parseInt(args[1]));
                } catch (Exception e) {
                    sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return true;
                }
            }

            for (Level level : sender.getServer().getLevels().values()) {
                level.checkTime();
                level.setTime(value);
                level.checkTime();
            }
            Command.broadcastCommandMessage(
                    sender, new TranslationContainer("commands.time.set", String.valueOf(value)));
        } else if ("add".equals(args[0])) {
            if (!sender.hasPermission("crimsonmc.command.time.add")) {
                sender.sendMessage(
                        new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));

                return true;
            }

            int value;
            try {
                value = Math.max(0, Integer.parseInt(args[1]));
            } catch (Exception e) {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }

            for (Level level : sender.getServer().getLevels().values()) {
                level.checkTime();
                level.setTime(level.getTime() + value);
                level.checkTime();
            }
            Command.broadcastCommandMessage(
                    sender, new TranslationContainer("commands.time.added", String.valueOf(value)));
        } else {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
        }

        return true;
    }
}
