package cn.nukkit.network.player.handler;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDoor;
import cn.nukkit.entity.item.EntityExpBottle;
import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.entity.projectile.EntityEgg;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.entity.projectile.EntitySnowball;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.sound.LaunchSound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.player.PacketHandler;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.network.protocol.UseItemPacket;

public class UseItemPacketHandler extends PacketHandler<UseItemPacket> {


    @Override
    public boolean handlePacket(Player player, UseItemPacket packet) {
        if (!player.isSpawned() || !player.isAlive() || player.blocked) {
            return false;
        }

        var yaw = player.yaw;
        var pitch = player.pitch;
        var x = player.x;
        var y = player.y;
        var z = player.z;

        Item item;
        int slot;

        Vector3 blockVector = new Vector3(packet.x, packet.y, packet.z);

        player.craftingType = Player.CRAFTING_SMALL;

        if (packet.face >= 0 && packet.face <= 5) {
            player.setDataFlag(Player.DATA_FLAGS, Player.DATA_FLAG_ACTION, false);

            if (!player.canInteract(blockVector.add(0.5, 0.5, 0.5), 13)) {
                // TODO: what?
            } else if (player.isCreative()) {
                Item i = player.getInventory().getItemInHand();
                if (player.level.useItemOn(blockVector, i, packet.face, packet.fx,
                        packet.fy, packet.fz, player
                ) != null) {
                    return false;
                }
            } else if (!player.getInventory().getItemInHand().deepEquals(packet.item)) {
                player.getInventory().sendHeldItem(player);
            } else {
                item = player.getInventory().getItemInHand();
                Item oldItem = item.clone();
                // TODO: Implement adventure mode checks
                if ((item = player.level.useItemOn(blockVector, item, packet.face, packet.fx,
                        packet.fy, packet.fz, player
                )) != null) {
                    if (!item.deepEquals(oldItem) || item.getCount() != oldItem.getCount()) {
                        player.getInventory().setItemInHand(item);
                        player.getInventory().sendHeldItem(player.getHasSpawned().values());
                    }
                    return false;
                }
            }

            player.getInventory().sendHeldItem(player);

            if (blockVector.distanceSquared(player) > 10000) {
                return false;
            }

            Block target = player.level.getBlock(blockVector);
            Block block = target.getSide(packet.face);

            if (target instanceof BlockDoor door) {

                Block part;

                if ((door.getDamage() & 0x08) > 0) { // up
                    part = target.getSide(Vector3.SIDE_DOWN);

                    if (part.getId() == target.getId()) {
                        target = part;
                    }
                }
            }

            player.level.sendBlocks(new Player[]{player}, new Block[]{target, block},
                    UpdateBlockPacket.FLAG_ALL_PRIORITY
            );
            return false;
        } else if (packet.face == 0xff) {
            Vector3 aimPos = (new Vector3((double) packet.x / 32768, (double) packet.y / 32768,
                    (double) packet.z / 32768
            )).normalize();

            if (player.isCreative()) {
                item = player.getInventory().getItemInHand();
            } else if (!player.getInventory().getItemInHand().deepEquals(packet.item)) {
                player.getInventory().sendHeldItem(player);
                return false;
            } else {
                item = player.getInventory().getItemInHand();
            }

            PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(player, item, aimPos,
                    packet.face, PlayerInteractEvent.RIGHT_CLICK_AIR
            );

            player.getServer().getPluginManager().callEvent(playerInteractEvent);

            if (playerInteractEvent.isCancelled()) {
                player.getInventory().sendHeldItem(player);
                return false;
            }

            if (item.getId() == Item.SNOWBALL) {
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<DoubleTag>("Pos")
                                .add(new DoubleTag("", player.x))
                                .add(new DoubleTag("", player.y + player.getEyeHeight()))
                                .add(new DoubleTag("", player.z)))
                        .putList(new ListTag<DoubleTag>("Motion")
                                /*
                                 * aimPos.x
                                 * aimPos.y
                                 * aimPos.z
                                 */
                                .add(new DoubleTag(
                                        "",
                                        -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)
                                ))
                                .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
                                .add(new DoubleTag(
                                        "",
                                        Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)
                                )))
                        .putList(new ListTag<FloatTag>("Rotation")
                                .add(new FloatTag("", (float) player.yaw))
                                .add(new FloatTag("", (float) player.pitch)));

                float f = 1.5f;
                EntitySnowball snowball = new EntitySnowball(player.chunk, nbt, player);

                snowball.setMotion(snowball.getMotion().multiply(f));

                if (player.isSurvival()) {
                    item.setCount(item.getCount() - 1);
                    player.getInventory().setItemInHand(item.getCount() > 0 ? item : Item.get(Item.AIR));
                }

                ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(snowball);
                player.getServer().getPluginManager().callEvent(projectileLaunchEvent);
                if (projectileLaunchEvent.isCancelled()) {
                    snowball.kill();
                } else {
                    snowball.spawnToAll();
                    player.level.addSound(new LaunchSound(player), player.getViewers().values());
                }
            } else if (item.getId() == Item.EGG) {
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<DoubleTag>("Pos")
                                .add(new DoubleTag("", player.x))
                                .add(new DoubleTag("", player.y + player.getEyeHeight()))
                                .add(new DoubleTag("", player.z)))
                        .putList(new ListTag<DoubleTag>("Motion")
                                /*
                                 * .add(new DoubleTag("", aimPos.x))
                                 * .add(new DoubleTag("", aimPos.y))
                                 * .add(new DoubleTag("", aimPos.z)))
                                 */
                                .add(new DoubleTag(
                                        "",
                                        -Math.sin(yaw / 180 * Math.PI) * Math.cos(pitch / 180 * Math.PI)
                                ))
                                .add(new DoubleTag("", -Math.sin(pitch / 180 * Math.PI)))
                                .add(new DoubleTag(
                                        "",
                                        Math.cos(yaw / 180 * Math.PI) * Math.cos(pitch / 180 * Math.PI)
                                )))
                        .putList(new ListTag<FloatTag>("Rotation")
                                .add(new FloatTag("", (float) yaw))
                                .add(new FloatTag("", (float) pitch)));

                float f = 1.5f;
                EntityEgg egg = new EntityEgg(player.chunk, nbt, player);

                egg.setMotion(egg.getMotion().multiply(f));
                if (player.isSurvival()) {
                    item.setCount(item.getCount() - 1);
                    player.getInventory().setItemInHand(item.getCount() > 0 ? item : Item.get(Item.AIR));
                }
                ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(egg);
                player.getServer().getPluginManager().callEvent(projectileLaunchEvent);
                if (projectileLaunchEvent.isCancelled()) {
                    egg.kill();
                } else {
                    egg.spawnToAll();
                    player.level.addSound(new LaunchSound(player), player.getViewers().values());
                }
            } else if (item.getId() == Item.EXPERIENCE_BOTTLE) {
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<DoubleTag>("Pos")
                                .add(new DoubleTag("", x))
                                .add(new DoubleTag("", y + player.getEyeHeight()))
                                .add(new DoubleTag("", z)))
                        .putList(new ListTag<DoubleTag>("Motion")
                                .add(new DoubleTag(
                                        "",
                                        -Math.sin(yaw / 180 * Math.PI) * Math.cos(pitch / 180 * Math.PI)
                                ))
                                .add(new DoubleTag("", -Math.sin(pitch / 180 * Math.PI)))
                                .add(new DoubleTag(
                                        "",
                                        Math.cos(yaw / 180 * Math.PI) * Math.cos(pitch / 180 * Math.PI)
                                )))
                        .putList(new ListTag<FloatTag>("Rotation")
                                .add(new FloatTag("", (float) yaw))
                                .add(new FloatTag("", (float) pitch)))
                        .putInt("Potion", item.getDamage());
                double f = 1.5;
                EntityProjectile bottle = new EntityExpBottle(player.chunk, nbt, player);
                bottle.setMotion(bottle.getMotion().multiply(f));
                if (player.isSurvival()) {
                    item.setCount(item.getCount() - 1);
                    player.getInventory().setItemInHand(item.getCount() > 0 ? item : Item.get(Item.AIR));
                }
                EntityProjectile bottleEntity = bottle;
                ProjectileLaunchEvent projectileEv = new ProjectileLaunchEvent(bottleEntity);
                player.getServer().getPluginManager().callEvent(projectileEv);
                if (projectileEv.isCancelled()) {
                    bottle.kill();
                } else {
                    bottle.spawnToAll();
                    player.level.addSound(new LaunchSound(player), player.getViewers().values());
                }
            } else if (item.getId() == Item.SPLASH_POTION) {
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<DoubleTag>("Pos")
                                .add(new DoubleTag("", x))
                                .add(new DoubleTag("", y + player.getEyeHeight()))
                                .add(new DoubleTag("", z)))
                        .putList(new ListTag<DoubleTag>("Motion")
                                .add(new DoubleTag(
                                        "",
                                        -Math.sin(yaw / 180 * Math.PI) * Math.cos(pitch / 180 * Math.PI)
                                ))
                                .add(new DoubleTag("", -Math.sin(pitch / 180 * Math.PI)))
                                .add(new DoubleTag(
                                        "",
                                        Math.cos(yaw / 180 * Math.PI) * Math.cos(pitch / 180 * Math.PI)
                                )))
                        .putList(new ListTag<FloatTag>("Rotation")
                                .add(new FloatTag("", (float) yaw))
                                .add(new FloatTag("", (float) pitch)))
                        .putShort("PotionId", item.getDamage());
                double f = 1.5;
                EntityPotion bottle = new EntityPotion(player.chunk, nbt, player);
                bottle.setMotion(bottle.getMotion().multiply(f));
                if (player.isSurvival()) {
                    item.setCount(item.getCount() - 1);
                    player.getInventory().setItemInHand(item.getCount() > 0 ? item : Item.get(Item.AIR));
                }
                ProjectileLaunchEvent projectileEv = new ProjectileLaunchEvent(bottle);

                player.getServer().getPluginManager().callEvent(projectileEv);
                if (projectileEv.isCancelled()) {
                    bottle.kill();
                } else {
                    bottle.spawnToAll();
                    player.level.addSound(new LaunchSound(player), player.getViewers().values());
                }
            }

            player.setDataFlag(Player.DATA_FLAGS, Player.DATA_FLAG_ACTION, true);
            player.setStartAction(player.getServer().getTick());
        }
        return false;

    }
}
