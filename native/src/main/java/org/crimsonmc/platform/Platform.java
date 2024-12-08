package org.crimsonmc.platform;

public abstract class Platform {

    public static boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    public static boolean IS_OSX = System.getProperty("os.name").toLowerCase().contains("osx");

}
