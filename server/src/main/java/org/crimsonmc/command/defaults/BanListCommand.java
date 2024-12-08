package org.crimsonmc.command.defaults;

import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.permission.BanEntry;
import org.crimsonmc.permission.BanList;

import java.util.LinkedHashMap;

/**
 * Created on 2015/11/11 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class BanListCommand extends VanillaCommand {

    public BanListCommand(String name) {
        super(name, "%crimsonmc.command.banlist.description", "%commands.banlist.usage");
        this.setPermission("crimsonmc.command.ban.list");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        BanList list;
        String arg;
        if (args.length > 0) {
            arg = args[0].toLowerCase();
            if ("ips".equals(arg)) {
                list = sender.getServer().getIPBans();
            } else if ("players".equals(arg)) {
                list = sender.getServer().getNameBans();
            } else {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));

                return false;
            }
        } else {
            list = sender.getServer().getNameBans();
            arg = "players";
        }

        String message = "";
        LinkedHashMap<String, BanEntry> entries = list.getEntires();
        for (BanEntry entry : entries.values()) {
            message += entry.getName() + ", ";
        }

        if ("ips".equals(arg)) {
            sender.sendMessage(
                    new TranslationContainer("commands.banlist.ips", String.valueOf(entries.size())));
        } else {
            sender.sendMessage(
                    new TranslationContainer("commands.banlist.players", String.valueOf(entries.size())));
        }

        if (message.length() > 0) {
            message = message.substring(0, message.length() - 2);
        }
        sender.sendMessage(message);

        return true;
    }
}
