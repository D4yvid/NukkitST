package org.crimsonmc.command.defaults;

import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.network.protocol.ProtocolInfo;
import org.crimsonmc.plugin.Plugin;
import org.crimsonmc.plugin.PluginDescription;
import org.crimsonmc.text.TextFormat;

import java.util.List;

/**
 * Created on 2015/11/12 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class VersionCommand extends VanillaCommand {

    public VersionCommand(String name) {
        super(name, "%crimsonmc.command.version.description", "%crimsonmc.command.version.usage",
                new String[]{"ver", "about"});
        this.setPermission("crimsonmc.command.version");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(new TranslationContainer(
                    "crimsonmc.server.info.extended",
                    new String[]{sender.getServer().getName(), sender.getServer().getServerVersion(),
                            sender.getServer().getCodename(), sender.getServer().getApiVersion(),
                            sender.getServer().getVersion(),
                            String.valueOf(ProtocolInfo.CURRENT_PROTOCOL)}));
        } else {
            String pluginName = "";
            for (String arg : args)
                pluginName += arg + " ";
            pluginName = pluginName.trim();
            final boolean[] found = {false};
            final Plugin[] exactPlugin = {sender.getServer().getPluginManager().getPlugin(pluginName)};

            if (exactPlugin[0] == null) {
                pluginName = pluginName.toLowerCase();
                final String finalPluginName = pluginName;
                sender.getServer().getPluginManager().getPlugins().forEach((s, p) -> {
                    if (s.toLowerCase().contains(finalPluginName)) {
                        exactPlugin[0] = p;
                        found[0] = true;
                    }
                });
            } else {
                found[0] = true;
            }

            if (found[0]) {
                PluginDescription desc = exactPlugin[0].getDescription();
                sender.sendMessage(TextFormat.DARK_GREEN + desc.getName() + TextFormat.WHITE + " version " +
                        TextFormat.DARK_GREEN + desc.getVersion());
                if (desc.getDescription() != null) {
                    sender.sendMessage(desc.getDescription());
                }
                if (desc.getWebsite() != null) {
                    sender.sendMessage("Website: " + desc.getWebsite());
                }
                List<String> authors = desc.getAuthors();
                final String[] authorsString = {""};
                authors.forEach((s) -> authorsString[0] += s);
                if (authors.size() == 1) {
                    sender.sendMessage("Author: " + authorsString[0]);
                } else if (authors.size() >= 2) {
                    sender.sendMessage("Authors: " + authorsString[0]);
                }
            } else {
                sender.sendMessage(new TranslationContainer("crimsonmc.command.version.noSuchPlugin"));
            }
        }
        return true;
    }
}
