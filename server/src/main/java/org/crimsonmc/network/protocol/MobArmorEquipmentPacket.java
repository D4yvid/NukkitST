package org.crimsonmc.network.protocol;

import org.crimsonmc.item.Item;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class MobArmorEquipmentPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET;

    public long eid;

    public Item[] slots = new Item[4];

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.eid = this.getLong();
        this.slots = new Item[4];
        this.slots[0] = this.getSlot();
        this.slots[1] = this.getSlot();
        this.slots[2] = this.getSlot();
        this.slots[3] = this.getSlot();
    }

    @Override
    public void encode() {
        this.reset();
        this.putLong(this.eid);
        this.putSlot(this.slots[0]);
        this.putSlot(this.slots[1]);
        this.putSlot(this.slots[2]);
        this.putSlot(this.slots[3]);
    }
}
