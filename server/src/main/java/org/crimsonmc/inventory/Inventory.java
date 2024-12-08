package org.crimsonmc.inventory;

import org.crimsonmc.item.Item;
import org.crimsonmc.player.ServerPlayer;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * author: MagicDroidX crimsonmc Project
 */
public interface Inventory {

    int MAX_STACK = 64;

    int getSize();

    int getMaxStackSize();

    void setMaxStackSize(int size);

    String getName();

    String getTitle();

    Item getItem(int index);

    boolean setItem(int index, Item item);

    Item[] addItem(Item... slots);

    boolean canAddItem(Item item);

    Item[] removeItem(Item... slots);

    Map<Integer, Item> getContents();

    void setContents(Map<Integer, Item> items);

    void sendContents(ServerPlayer player);

    void sendContents(ServerPlayer[] players);

    void sendContents(Collection<ServerPlayer> players);

    void sendSlot(int index, ServerPlayer player);

    void sendSlot(int index, ServerPlayer[] players);

    void sendSlot(int index, Collection<ServerPlayer> players);

    boolean contains(Item item);

    Map<Integer, Item> all(Item item);

    int first(Item item);

    int firstEmpty(Item item);

    void remove(Item item);

    boolean clear(int index);

    void clearAll();

    Set<ServerPlayer> getViewers();

    InventoryType getType();

    InventoryHolder getHolder();

    void onOpen(ServerPlayer who);

    boolean open(ServerPlayer who);

    void close(ServerPlayer who);

    void onClose(ServerPlayer who);

    void onSlotChange(int index, Item before);
}
