package org.crimsonmc.command.defaults;

import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.plugin.Plugin;
import org.crimsonmc.text.TextFormat;

import java.util.Map;

/**
 * Created on 2015/11/12 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class PluginsCommand extends VanillaCommand {

    public PluginsCommand(String name) {
        super(name, "%crimsonmc.command.plugins.description", "%crimsonmc.command.plugins.usage",
                new String[]{"pl"});
        this.setPermission("crimsonmc.command.plugins");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        this.sendPluginList(sender);
        return true;
    }

    private void sendPluginList(CommandSender sender) {
        String list = "";
        Map<String, Plugin> plugins = sender.getServer().getPluginManager().getPlugins();
        for (Plugin plugin : plugins.values()) {
            if (list.length() > 0) {
                list += TextFormat.WHITE + ", ";
            }
            list += plugin.isEnabled() ? TextFormat.GREEN : TextFormat.RED;
            list += plugin.getDescription().getFullName();
        }

        sender.sendMessage(new TranslationContainer(
                "crimsonmc.command.plugins.success", new String[]{String.valueOf(plugins.size()), list}));
    }
}
