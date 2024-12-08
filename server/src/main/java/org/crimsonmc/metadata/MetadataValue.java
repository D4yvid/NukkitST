package org.crimsonmc.metadata;

import org.crimsonmc.plugin.Plugin;

import java.lang.ref.WeakReference;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class MetadataValue {

    protected final WeakReference<Plugin> owningPlugin;

    protected MetadataValue(Plugin owningPlugin) {
        this.owningPlugin = new WeakReference<>(owningPlugin);
    }

    public Plugin getOwningPlugin() {
        return this.owningPlugin.get();
    }

    public abstract Object value();

    public abstract void invalidate();
}
