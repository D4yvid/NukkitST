package org.crimsonmc.event.plugin;

import org.crimsonmc.plugin.Plugin;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class PluginDisableEvent extends PluginEvent {

    public PluginDisableEvent(Plugin plugin) {
        super(plugin);
    }
}
