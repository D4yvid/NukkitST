package org.crimsonmc.inventory;

import org.crimsonmc.block.BlockAir;
import org.crimsonmc.entity.EntityHuman;
import org.crimsonmc.entity.EntityHumanType;
import org.crimsonmc.event.entity.EntityArmorChangeEvent;
import org.crimsonmc.event.entity.EntityInventoryChangeEvent;
import org.crimsonmc.event.player.PlayerItemHeldEvent;
import org.crimsonmc.item.Item;
import org.crimsonmc.item.ItemBlock;
import org.crimsonmc.network.protocol.ContainerSetContentPacket;
import org.crimsonmc.network.protocol.ContainerSetSlotPacket;
import org.crimsonmc.network.protocol.MobArmorEquipmentPacket;
import org.crimsonmc.network.protocol.MobEquipmentPacket;
import org.crimsonmc.player.ServerPlayer;
import org.crimsonmc.server.Server;

import java.util.Arrays;
import java.util.Collection;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class PlayerInventory extends BaseInventory {

    protected final int[] hotbar;

    protected int itemInHandIndex = 0;

    public PlayerInventory(EntityHumanType player) {
        super(player, InventoryType.PLAYER);
        this.hotbar = new int[this.getHotbarSize()];
        Arrays.fill(this.hotbar, -1);
    }

    @Override
    public int getSize() {
        return super.getSize() - 4;
    }

    @Override
    public void setSize(int size) {
        super.setSize(size + 4);
        this.sendContents(this.getViewers());
    }

    public int getHotbarSlotIndex(int index) {
        return (index >= 0 && index < this.getHotbarSize()) ? this.hotbar[index] : -1;
    }

    public void setHotbarSlotIndex(int index, int slot) {
        if (index >= 0 && index < this.getHotbarSize() && slot >= -1 && slot < this.getSize()) {
            this.hotbar[index] = slot;
        }
    }

    public int getHeldItemIndex() {
        return itemInHandIndex;
    }

    public void setHeldItemIndex(int index) {
        if (index >= 0 && index < this.getHotbarSize()) {
            this.itemInHandIndex = index;

            this.sendHeldItem(this.getHolder().getViewers().values());
        }
    }

    public Item getItemInHand() {
        Item item = this.getItem(this.getHeldItemSlot());
        if (item != null) {
            return item;
        } else {
            return new ItemBlock(new BlockAir(), 0, 0);
        }
    }

    public boolean setItemInHand(Item item) {
        return this.setItem(this.getHeldItemSlot(), item);
    }

    public int getHeldItemSlot() {
        return this.getHotbarSlotIndex(this.itemInHandIndex);
    }

    public void setHeldItemSlot(int slot) {
        if (slot >= -1 && slot < this.getSize()) {
            Item item = this.getItem(slot);

            int itemIndex = this.getHeldItemIndex();

            if (this.getHolder() instanceof ServerPlayer) {
                PlayerItemHeldEvent ev =
                        new PlayerItemHeldEvent((ServerPlayer) this.getHolder(), item, slot, itemIndex);
                Server.getInstance().getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    this.sendContents((ServerPlayer) this.getHolder());
                    return;
                }
            }

            this.setHotbarSlotIndex(itemIndex, slot);
        }
    }

    public void sendHeldItem(ServerPlayer player) {
        Item item = this.getItemInHand();

        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = player.equals(this.getHolder()) ? 0 : this.getHolder().getId();
        pk.item = item;
        pk.slot = (byte) this.getHeldItemSlot();
        pk.selectedSlot = (byte) this.getHeldItemIndex();

        player.dataPacket(pk);
        if (player.equals(this.getHolder())) {
            this.sendSlot(this.getHeldItemSlot(), player);
        }
    }

    public void sendHeldItem(ServerPlayer[] players) {
        Item item = this.getItemInHand();

        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.item = item;
        pk.slot = (byte) this.getHeldItemSlot();
        pk.selectedSlot = (byte) this.getHeldItemIndex();

        for (ServerPlayer player : players) {
            pk.eid = this.getHolder().getId();
            if (player.equals(this.getHolder())) {
                pk.eid = 0;
                this.sendSlot(this.getHeldItemSlot(), player);
            }

            player.dataPacket(pk);
        }
    }

    public void sendHeldItem(Collection<ServerPlayer> players) {
        this.sendHeldItem(players.stream().toArray(ServerPlayer[]::new));
    }

    @Override
    public void onSlotChange(int index, Item before) {
        EntityHuman holder = this.getHolder();
        if (holder instanceof ServerPlayer && !((ServerPlayer) holder).isSpawned()) {
            return;
        }

        super.onSlotChange(index, before);

        if (index >= this.getSize()) {
            this.sendArmorSlot(index, this.getViewers());
            this.sendArmorSlot(index, this.getHolder().getViewers().values());
        }
    }

    public int getHotbarSize() {
        return 9;
    }

    public Item getArmorItem(int index) {
        return this.getItem(this.getSize() + index);
    }

    public boolean setArmorItem(int index, Item item) {
        return this.setItem(this.getSize() + index, item);
    }

    public Item getHelmet() {
        return this.getItem(this.getSize());
    }

    public Item getChestplate() {
        return this.getItem(this.getSize() + 1);
    }

    public Item getLeggings() {
        return this.getItem(this.getSize() + 2);
    }

    public Item getBoots() {
        return this.getItem(this.getSize() + 3);
    }

    public boolean setHelmet(Item helmet) {
        return this.setItem(this.getSize(), helmet);
    }

    public boolean setChestplate(Item chestplate) {
        return this.setItem(this.getSize() + 1, chestplate);
    }

    public boolean setLeggings(Item leggings) {
        return this.setItem(this.getSize() + 2, leggings);
    }

    public boolean setBoots(Item boots) {
        return this.setItem(this.getSize() + 3, boots);
    }

    @Override
    public boolean setItem(int index, Item item) {
        if (index < 0 || index >= this.size) {
            return false;
        } else if (item.getId() == 0 || item.getCount() <= 0) {
            return this.clear(index);
        }

        // Armor change
        if (index >= this.getSize()) {
            EntityArmorChangeEvent ev =
                    new EntityArmorChangeEvent(this.getHolder(), this.getItem(index), item, index);
            Server.getInstance().getPluginManager().callEvent(ev);
            if (ev.isCancelled() && this.getHolder() != null) {
                this.sendArmorSlot(index, this.getViewers());
                return false;
            }
            item = ev.getNewItem();
        } else {
            EntityInventoryChangeEvent ev =
                    new EntityInventoryChangeEvent(this.getHolder(), this.getItem(index), item, index);
            Server.getInstance().getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                this.sendSlot(index, this.getViewers());
                return false;
            }
            item = ev.getNewItem();
        }

        Item old = this.getItem(index);
        this.slots.put(index, item.clone());
        this.onSlotChange(index, old);

        return true;
    }

    @Override
    public boolean clear(int index) {
        if (this.slots.containsKey(index)) {
            Item item = new ItemBlock(new BlockAir(), null, 0);
            Item old = this.slots.get(index);
            if (index >= this.getSize() && index < this.size) {
                EntityArmorChangeEvent ev = new EntityArmorChangeEvent(this.getHolder(), old, item, index);
                Server.getInstance().getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    if (index >= this.size) {
                        this.sendArmorSlot(index, this.getViewers());
                    } else {
                        this.sendSlot(index, this.getViewers());
                    }
                    return false;
                }
                item = ev.getNewItem();
            } else {
                EntityInventoryChangeEvent ev =
                        new EntityInventoryChangeEvent(this.getHolder(), old, item, index);
                Server.getInstance().getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    if (index >= this.size) {
                        this.sendArmorSlot(index, this.getViewers());
                    } else {
                        this.sendSlot(index, this.getViewers());
                    }
                    return false;
                }
                item = ev.getNewItem();
            }

            if (item.getId() != Item.AIR) {
                this.slots.put(index, item.clone());
            } else {
                this.slots.remove(index);
            }

            this.onSlotChange(index, old);
        }

        return true;
    }

    public Item[] getArmorContents() {
        Item[] armor = new Item[4];
        for (int i = 0; i < 4; i++) {
            armor[i] = this.getItem(this.getSize() + i);
        }

        return armor;
    }

    public void setArmorContents(Item[] items) {
        if (items.length < 4) {
            Item[] newItems = new Item[4];
            System.arraycopy(items, 0, newItems, 0, items.length);
            items = newItems;
        }

        for (int i = 0; i < 4; ++i) {
            if (items[i] == null) {
                items[i] = new ItemBlock(new BlockAir(), null, 0);
            }

            if (items[i].getId() == Item.AIR) {
                this.clear(this.getSize() + i);
            } else {
                this.setItem(this.getSize() + 1, items[i]);
            }
        }
    }

    @Override
    public void clearAll() {
        int limit = this.getSize() + 4;
        for (int index = 0; index < limit; ++index) {
            this.clear(index);
        }
    }

    public void sendArmorContents(ServerPlayer player) {
        this.sendArmorContents(new ServerPlayer[]{player});
    }

    public void sendArmorContents(ServerPlayer[] players) {
        Item[] armor = this.getArmorContents();

        MobArmorEquipmentPacket pk = new MobArmorEquipmentPacket();
        pk.eid = this.getHolder().getId();
        pk.slots = armor;
        pk.encode();
        pk.isEncoded = true;

        for (ServerPlayer player : players) {
            if (player.equals(this.getHolder())) {
                ContainerSetContentPacket pk2 = new ContainerSetContentPacket();
                pk2.windowid = ContainerSetContentPacket.SPECIAL_ARMOR;
                pk2.slots = armor;
                player.dataPacket(pk2);
            } else {
                player.dataPacket(pk);
            }
        }
    }

    public void sendArmorContents(Collection<ServerPlayer> players) {
        this.sendArmorContents(players.stream().toArray(ServerPlayer[]::new));
    }

    public void sendArmorSlot(int index, ServerPlayer player) {
        this.sendArmorSlot(index, new ServerPlayer[]{player});
    }

    public void sendArmorSlot(int index, ServerPlayer[] players) {
        Item[] armor = this.getArmorContents();

        MobArmorEquipmentPacket pk = new MobArmorEquipmentPacket();
        pk.eid = this.getHolder().getId();
        pk.slots = armor;
        pk.encode();
        pk.isEncoded = true;

        for (ServerPlayer player : players) {
            if (player.equals(this.getHolder())) {
                ContainerSetSlotPacket pk2 = new ContainerSetSlotPacket();
                pk2.windowid = ContainerSetContentPacket.SPECIAL_ARMOR;
                pk2.slot = index - this.getSize();
                pk2.item = this.getItem(index);
                player.dataPacket(pk2);
            } else {
                player.dataPacket(pk);
            }
        }
    }

    public void sendArmorSlot(int index, Collection<ServerPlayer> players) {
        this.sendArmorSlot(index, players.stream().toArray(ServerPlayer[]::new));
    }

    @Override
    public void sendContents(ServerPlayer player) {
        this.sendContents(new ServerPlayer[]{player});
    }

    @Override
    public void sendContents(Collection<ServerPlayer> players) {
        this.sendContents(players.stream().toArray(ServerPlayer[]::new));
    }

    @Override
    public void sendContents(ServerPlayer[] players) {
        ContainerSetContentPacket pk = new ContainerSetContentPacket();
        pk.slots = new Item[this.getSize()];
        for (int i = 0; i < this.getSize(); ++i) {
            pk.slots[i] = this.getItem(i);
        }

        for (ServerPlayer player : players) {
            if (player.equals(this.getHolder())) {
                pk.hotbar = new int[this.getHotbarSize()];
                for (int i = 0; i < this.getHotbarSize(); ++i) {
                    int index = this.getHotbarSlotIndex(i);
                    pk.hotbar[i] = index <= -1 ? -1 : index + 9;
                }
            }
            int id = player.getWindowId(this);
            if (id == -1 || !player.isSpawned()) {
                this.close(player);
                continue;
            }
            pk.windowid = (byte) id;
            player.dataPacket(pk.clone());
        }
    }

    @Override
    public void sendSlot(int index, ServerPlayer player) {
        this.sendSlot(index, new ServerPlayer[]{player});
    }

    @Override
    public void sendSlot(int index, Collection<ServerPlayer> players) {
        this.sendSlot(index, players.stream().toArray(ServerPlayer[]::new));
    }

    @Override
    public void sendSlot(int index, ServerPlayer[] players) {
        ContainerSetSlotPacket pk = new ContainerSetSlotPacket();
        pk.slot = index;
        pk.item = this.getItem(index).clone();

        for (ServerPlayer player : players) {
            if (player.equals(this.getHolder())) {
                pk.windowid = 0;
                player.dataPacket(pk);
            } else {
                int id = player.getWindowId(this);
                if (id == -1) {
                    this.close(player);
                    continue;
                }
                pk.windowid = (byte) id;
                player.dataPacket(pk.clone());
            }
        }
    }

    @Override
    public EntityHuman getHolder() {
        return (EntityHuman) super.getHolder();
    }
}
