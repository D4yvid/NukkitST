package org.crimsonmc.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class ThreadStore {

    public static final Map<String, Object> store = new ConcurrentHashMap<>();
}
