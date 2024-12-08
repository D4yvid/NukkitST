package org.crimsonmc.command.defaults;

import org.crimsonmc.command.CommandSender;
import org.crimsonmc.level.Level;
import org.crimsonmc.math.MathUtilities;
import org.crimsonmc.text.TextFormat;

/**
 * Created on 2015/11/11 by xtypr. Package cn.crimsonmc.command.defaults in project crimsonmc .
 */
public class GarbageCollectorCommand extends VanillaCommand {

    public GarbageCollectorCommand(String name) {
        super(name, "%crimsonmc.command.gc.description", "%crimsonmc.command.gc.usage");
        this.setPermission("crimsonmc.command.gc");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        int chunksCollected = 0;
        int entitiesCollected = 0;
        int tilesCollected = 0;
        long memory = Runtime.getRuntime().freeMemory();

        for (Level level : sender.getServer().getLevels().values()) {
            int chunksCount = level.getChunks().size();
            int entitiesCount = level.getEntities().length;
            int tilesCount = level.getBlockEntities().size();
            level.doChunkGarbageCollection();
            level.unloadChunks(true);
            chunksCollected += chunksCount - level.getChunks().size();
            entitiesCollected += entitiesCount - level.getEntities().length;
            tilesCollected += tilesCount - level.getBlockEntities().size();
            level.clearCache(true);
        }

        System.gc();

        long freedMemory = Runtime.getRuntime().freeMemory() - memory;

        sender.sendMessage(TextFormat.GREEN + "---- " + TextFormat.WHITE + "Garbage collection result" +
                TextFormat.GREEN + " ----");
        sender.sendMessage(TextFormat.GOLD + "Chunks: " + TextFormat.RED + chunksCollected);
        sender.sendMessage(TextFormat.GOLD + "Entities: " + TextFormat.RED + entitiesCollected);
        sender.sendMessage(TextFormat.GOLD + "Block Entities: " + TextFormat.RED + tilesCollected);
        sender.sendMessage(TextFormat.GOLD + "Memory freed: " + TextFormat.RED +
                MathUtilities.round((freedMemory / 1024d / 1024d), 2) + " MB");
        return true;
    }
}
