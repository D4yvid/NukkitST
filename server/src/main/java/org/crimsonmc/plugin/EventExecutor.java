package org.crimsonmc.plugin;

import org.crimsonmc.event.Event;
import org.crimsonmc.event.Listener;
import org.crimsonmc.exception.EventException;

/**
 * author: iNevet crimsonmc Project
 */
public interface EventExecutor {

    void execute(Listener listener, Event event) throws EventException;
}
