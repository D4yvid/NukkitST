package org.crimsonmc.inventory;

import org.crimsonmc.item.Item;
import org.crimsonmc.math.Vector3;
import org.crimsonmc.network.protocol.ContainerClosePacket;
import org.crimsonmc.network.protocol.ContainerOpenPacket;
import org.crimsonmc.player.ServerPlayer;

import java.util.Map;

/**
 * author: MagicDroidX crimsonmc Project
 */
public abstract class ContainerInventory extends BaseInventory {

    public ContainerInventory(InventoryHolder holder, InventoryType type) {
        super(holder, type);
    }

    public ContainerInventory(InventoryHolder holder, InventoryType type, Map<Integer, Item> items) {
        super(holder, type, items);
    }

    public ContainerInventory(InventoryHolder holder, InventoryType type, Map<Integer, Item> items,
                              Integer overrideSize) {
        super(holder, type, items, overrideSize);
    }

    public ContainerInventory(InventoryHolder holder, InventoryType type, Map<Integer, Item> items,
                              Integer overrideSize, String overrideTitle) {
        super(holder, type, items, overrideSize, overrideTitle);
    }

    @Override
    public void onOpen(ServerPlayer who) {
        super.onOpen(who);
        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowid = (byte) who.getWindowId(this);
        pk.type = (byte) this.getType().getNetworkType();
        pk.slots = this.getSize();
        InventoryHolder holder = this.getHolder();
        if (holder instanceof Vector3) {
            pk.x = (int) ((Vector3) holder).getX();
            pk.y = (int) ((Vector3) holder).getY();
            pk.z = (int) ((Vector3) holder).getZ();
        } else {
            pk.x = pk.y = pk.z = 0;
        }

        who.dataPacket(pk);

        this.sendContents(who);
    }

    @Override
    public void onClose(ServerPlayer who) {
        ContainerClosePacket pk = new ContainerClosePacket();
        pk.windowid = (byte) who.getWindowId(this);
        who.dataPacket(pk);
        super.onClose(who);
    }
}
