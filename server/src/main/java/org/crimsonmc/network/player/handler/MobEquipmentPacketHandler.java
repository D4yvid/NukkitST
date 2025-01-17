package org.crimsonmc.network.player.handler;

import org.crimsonmc.item.Item;
import org.crimsonmc.network.player.PacketHandler;
import org.crimsonmc.network.protocol.MobEquipmentPacket;
import org.crimsonmc.player.ServerPlayer;

public class MobEquipmentPacketHandler extends PacketHandler<MobEquipmentPacket> {

    @Override
    public boolean handlePacket(ServerPlayer player, MobEquipmentPacket packet) {
        Item item;
        int slot;

        if (!player.isSpawned() || !player.isAlive()) {
            return false;
        }

        // Determines if the slot overflowed the inventory limit of 27 items
        var overflowedSpace = (packet.slot > 27 || packet.slot < 0) && packet.slot != 255;
        var hotbarSize = player.getInventory().getHotbarSize();

        if (!overflowedSpace && packet.slot != 255) {
            packet.slot -= 9;
        }

        if (player.isCreative()) {
            item = packet.item;
            slot = Item.getCreativeItemIndex(item);
        } else {
            item = player.getInventory().getItem(packet.slot);
            slot = packet.slot;
        }

        if (overflowedSpace) {
            if (player.isCreative()) {
                player.sendHotbar();
                player.getInventory().sendContents(player);

                return false;
            }

            if (packet.selectedSlot >= 0 && packet.selectedSlot < 9) {
                player.getInventory().setHeldItemIndex(packet.selectedSlot);
                player.getInventory().setHeldItemSlot(packet.slot);
            }

            player.getInventory().sendContents(player);

            return false;
        } else if (item == null || slot == -1 || !item.deepEquals(packet.item)) {
            player.getInventory().sendContents(player);

            return false;
        } else if (player.isCreative()) {
            player.getInventory().setHeldItemIndex(packet.selectedSlot);
            player.getInventory().setItem(packet.selectedSlot, item);
            player.getInventory().setHeldItemSlot(packet.selectedSlot);
        } else {
            if (packet.selectedSlot >= 0 && packet.selectedSlot < hotbarSize) {
                player.getInventory().setHeldItemIndex(packet.selectedSlot);
                player.getInventory().setHeldItemSlot(slot);
            } else {
                player.getInventory().sendContents(player);
                return false;
            }
        }

        player.getInventory().sendHeldItem(player.getHasSpawned().values());

        player.setDataFlag(ServerPlayer.DATA_FLAGS, ServerPlayer.DATA_FLAG_ACTION, false);

        return false;
    }
}
