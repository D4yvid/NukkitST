package org.crimsonmc.event.plugin;

import org.crimsonmc.event.Event;
import org.crimsonmc.event.HandlerList;
import org.crimsonmc.plugin.Plugin;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class PluginEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Plugin plugin;

    public PluginEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
