package org.crimsonmc.command.defaults;

import org.crimsonmc.command.CommandSender;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.player.ServerPlayer;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class SeedCommand extends VanillaCommand {

    public SeedCommand(String name) {
        super(name, "%crimsonmc.command.seed.description", "%commands.seed.usage");
        this.setPermission("crimsonmc.command.seed");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        long seed;
        if (sender instanceof ServerPlayer) {
            seed = ((ServerPlayer) sender).getLevel().getSeed();
        } else {
            seed = sender.getServer().getDefaultLevel().getSeed();
        }

        sender.sendMessage(new TranslationContainer("commands.seed.success", String.valueOf(seed)));

        return true;
    }
}
