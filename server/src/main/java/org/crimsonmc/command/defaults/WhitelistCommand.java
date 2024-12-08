package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.text.TextFormat;

/**
 * Created on 2015/11/12 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class WhitelistCommand extends VanillaCommand {

    public WhitelistCommand(String name) {
        super(name, "%crimsonmc.command.whitelist.description", "%commands.whitelist.usage");
        this.setPermission("crimsonmc.command.whitelist.reload;"
                + "crimsonmc.command.whitelist.enable;"
                + "crimsonmc.command.whitelist.disable;"
                + "crimsonmc.command.whitelist.list;"
                + "crimsonmc.command.whitelist.add;"
                + "crimsonmc.command.whitelist.remove");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }

        if (args.length == 1) {
            if (this.badPerm(sender, args[0].toLowerCase())) {
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "reload":
                    sender.getServer().reloadWhitelist();
                    Command.broadcastCommandMessage(sender,
                            new TranslationContainer("commands.whitelist.reloaded"));

                    return true;
                case "on":
                    sender.getServer().setPropertyBoolean("white-list", true);
                    Command.broadcastCommandMessage(sender,
                            new TranslationContainer("commands.whitelist.enabled"));

                    return true;
                case "off":
                    sender.getServer().setPropertyBoolean("white-list", false);
                    Command.broadcastCommandMessage(sender,
                            new TranslationContainer("commands.whitelist.disabled"));

                    return true;
                case "list":
                    String result = "";
                    int count = 0;
                    for (String player : sender.getServer().getWhitelist().getAll().keySet()) {
                        result += player + ", ";
                        ++count;
                    }
                    sender.sendMessage(
                            new TranslationContainer("commands.whitelist.list",
                                    new String[]{String.valueOf(count), String.valueOf(count)}));
                    sender.sendMessage(result.length() > 0 ? result.substring(0, result.length() - 2) : "");

                    return true;

                case "add":
                    sender.sendMessage(
                            new TranslationContainer("commands.generic.usage", "%commands.whitelist.add.usage"));
                    return true;

                case "remove":
                    sender.sendMessage(
                            new TranslationContainer("commands.generic.usage", "%commands.whitelist.remove.usage"));
                    return true;
            }
        } else if (args.length == 2) {
            if (this.badPerm(sender, args[0].toLowerCase())) {
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "add":
                    sender.getServer().getOfflinePlayer(args[1]).setWhitelisted(true);
                    Command.broadcastCommandMessage(
                            sender, new TranslationContainer("commands.whitelist.add.success", args[1]));

                    return true;
                case "remove":
                    sender.getServer().getOfflinePlayer(args[1]).setWhitelisted(false);
                    Command.broadcastCommandMessage(
                            sender, new TranslationContainer("commands.whitelist.remove.success", args[1]));

                    return true;
            }
        }

        return true;
    }

    private boolean badPerm(CommandSender sender, String perm) {
        if (!sender.hasPermission("crimsonmc.command.whitelist" + perm)) {
            sender.sendMessage(new TranslationContainer(TextFormat.RED + ("%commands.generic." +
                    "permission")));

            return true;
        }

        return false;
    }
}
