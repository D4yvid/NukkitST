package org.crimsonmc.command.defaults;

import org.crimsonmc.command.Command;
import org.crimsonmc.command.CommandSender;
import org.crimsonmc.item.Item;
import org.crimsonmc.lang.TranslationContainer;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.text.TextFormat;

/**
 * Created on 2015/12/9 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class GiveCommand extends VanillaCommand {

    public GiveCommand(String name) {
        super(name, "%crimsonmc.command.give.description", "%crimsonmc.command.give.usage");
        this.setPermission("crimsonmc.command.give");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));

            return true;
        }

        ServerPlayer player = sender.getServer().getPlayer(args[0]);
        Item item;

        try {
            item = Item.fromString(args[1]);
        } catch (Exception e) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }

        try {
            item.setCount(Integer.parseInt(args[2]));
        } catch (Exception e) {
            item.setCount(item.getMaxStackSize());
        }

        if (player != null) {
            if (item.getId() == 0) {
                sender.sendMessage(
                        new TranslationContainer(TextFormat.RED + "%commands.give.item.notFound", args[1]));
                return true;
            }
            player.getInventory().addItem(item.clone());
        } else {
            sender.sendMessage(
                    new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));

            return true;
        }
        Command.broadcastCommandMessage(
                sender,
                new TranslationContainer(
                        "%commands.give.success",
                        new String[]{item.getName() + " (" + item.getId() + ":" + item.getDamage() + ")",
                                String.valueOf(item.getCount()), player.getName()}));
        return true;
    }
}
