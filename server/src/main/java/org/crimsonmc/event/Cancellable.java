package org.crimsonmc.event;

/**
 * Created by crimsonmc Team.
 */
public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean forceCancel);

    void setCancelled();
}
