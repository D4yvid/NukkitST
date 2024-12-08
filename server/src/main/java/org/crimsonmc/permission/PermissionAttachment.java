package org.crimsonmc.permission;

import org.crimsonmc.exception.PluginException;
import org.crimsonmc.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class PermissionAttachment {

    private final Map<String, Boolean> permissions = new HashMap<>();
    private final Permissible permissible;
    private final Plugin plugin;
    private PermissionRemovedExecutor removed = null;

    public PermissionAttachment(Plugin plugin, Permissible permissible) {
        if (!plugin.isEnabled()) {
            throw new PluginException("Plugin " + plugin.getDescription().getName() + " is disabled");
        }
        this.permissible = permissible;
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public PermissionRemovedExecutor getRemovalCallback() {
        return removed;
    }

    public void setRemovalCallback(PermissionRemovedExecutor executor) {
        this.removed = executor;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
        for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
            String key = entry.getKey();
            Boolean value = entry.getValue();
            this.permissions.put(key, value);
        }
        this.permissible.recalculatePermissions();
    }

    public void clearPermissions() {
        this.permissions.clear();
        this.permissible.recalculatePermissions();
    }

    public void unsetPermissions(List<String> permissions) {
        for (String node : permissions) {
            this.permissions.remove(node);
        }
        this.permissible.recalculatePermissions();
    }

    public void setPermission(Permission permission, boolean value) {
        this.setPermission(permission.getName(), value);
    }

    public void setPermission(String name, boolean value) {
        if (this.permissions.containsKey(name)) {
            if (this.permissions.get(name).equals(value)) {
                return;
            }
            this.permissions.remove(name);
        }
        this.permissions.put(name, value);
        this.permissible.recalculatePermissions();
    }

    public void unsetPermission(Permission permission, boolean value) {
        this.unsetPermission(permission.getName(), value);
    }

    public void unsetPermission(String name, boolean value) {
        if (this.permissions.containsKey(name)) {
            this.permissions.remove(name);
            this.permissible.recalculatePermissions();
        }
    }

    public void remove() {
        this.permissible.removeAttachment(this);
    }
}
