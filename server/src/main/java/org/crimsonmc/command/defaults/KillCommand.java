package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.event.entity.EntityDamageEvent;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.text.TextFormat;

/**
 * Created on 2015/12/08 by Pub4Game. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class KillCommand extends VanillaCommand {

    public KillCommand(String name) {
        super(name, "%crimsonmc.command.kill.description", "%crimsonmc.command.kill.usage",
                new String[]{"suicide"});
        this.setPermission("crimsonmc.command.kill.self;"
                + "crimsonmc.command.kill.other");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length >= 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        if (args.length == 1) {
            if (!sender.hasPermission("crimsonmc.command.kill.other")) {
                sender.sendMessage(
                        new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                return true;
            }
            ServerPlayer player = sender.getServer().getPlayer(args[0]);
            if (player != null) {
                EntityDamageEvent ev = new EntityDamageEvent(player, EntityDamageEvent.CAUSE_SUICIDE, 1000);
                sender.getServer().getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    return true;
                }
                player.setLastDamageCause(ev);
                player.setHealth(0);
                Command.broadcastCommandMessage(
                        sender, new TranslationContainer("commands.kill.successful", player.getName()));
            } else {
                sender.sendMessage(
                        new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
            }
            return true;
        }
        if (sender instanceof ServerPlayer) {
            if (!sender.hasPermission("crimsonmc.command.kill.self")) {
                sender.sendMessage(
                        new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                return true;
            }
            EntityDamageEvent ev =
                    new EntityDamageEvent((ServerPlayer) sender, EntityDamageEvent.CAUSE_SUICIDE, 1000);
            sender.getServer().getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return true;
            }
            ((ServerPlayer) sender).setLastDamageCause(ev);
            ((ServerPlayer) sender).setHealth(0);
            sender.sendMessage(new TranslationContainer("commands.kill.successful", sender.getName()));
        } else {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        return true;
    }
}
