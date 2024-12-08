package org.crimsonmc.command;

import java.util.List;

/**
 * author: MagicDroidX crimsonmc Project
 */
public interface CommandMap {

    void registerAll(String fallbackPrefix, List<? extends Command> commands);

    boolean register(String fallbackPrefix, Command command);

    boolean register(String fallbackPrefix, Command command, String label);

    void registerSimpleCommands(Object object);

    boolean dispatch(CommandSender sender, String cmdLine);

    void clearCommands();

    Command getCommand(String name);
}
