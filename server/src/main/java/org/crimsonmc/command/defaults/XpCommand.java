package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.text.TextFormat;

/**
 * Created by Snake1999 on 2016/1/22. Package cn.crimsonmc.command.defaults in project crimsonmc.
 */
public class XpCommand extends Command {

    public XpCommand(String name) {
        super(name, "%crimsonmc.command.xp.description", "%commands.xp.usage");
        this.setPermission("crimsonmc.command.xp");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        //  "/xp <amount> [player]"  for adding exp
        //  "/xp <amount>L [player]" for adding exp level
        String amountString;
        String playerName = "";
        ServerPlayer player = null;
        if (!(sender instanceof ServerPlayer)) {
            if (args.length != 2) {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
            amountString = args[0];
            playerName = args[1];
            player = sender.getServer().getPlayer(playerName);
        } else {
            if (args.length == 1) {
                amountString = args[0];
                player = (ServerPlayer) sender;
            } else if (args.length == 2) {
                amountString = args[0];
                playerName = args[1];
                player = sender.getServer().getPlayer(playerName);
            } else {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
        }

        if (player == null) {
            sender.sendMessage(
                    new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
            return true;
        }

        int amount;
        boolean isLevel = false;
        if (amountString.endsWith("l") || amountString.endsWith("L")) {
            amountString = amountString.substring(0, amountString.length() - 1);
            isLevel = true;
        }

        try {
            amount = Integer.parseInt(amountString);
        } catch (NumberFormatException e1) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }

        if (isLevel) {
            int newLevel = player.getExperienceLevel();
            newLevel += amount;
            if (newLevel > 24791)
                newLevel = 24791;
            if (newLevel < 0) {
                player.setExperience(0, 0);
            } else {
                player.setExperience(player.getExperience(), newLevel);
            }
            if (amount > 0) {
                sender.sendMessage(new TranslationContainer(
                        "commands.xp.success.levels", new String[]{String.valueOf(amount), player.getName()}));
            } else {
                sender.sendMessage(
                        new TranslationContainer("commands.xp.success.levels.minus",
                                new String[]{String.valueOf(-amount), player.getName()}));
            }
            return true;
        } else {
            if (amount < 0) {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
            player.addExperience(amount);
            sender.sendMessage(new TranslationContainer(
                    "commands.xp.success", new String[]{String.valueOf(amount), player.getName()}));
            return true;
        }
    }
}
