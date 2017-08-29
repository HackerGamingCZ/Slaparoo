package cz.hackergamingcz.slaparoo.Handlers;

import cz.hackergamingcz.slaparoo.Mechanics;
import cz.hackergamingcz.slaparoo.Threads.SpeedBoostSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class SpeedBoost implements Listener{
    private static World world = Bukkit.getWorld("world");

    public static Location randomSpawn(){
        Location spawnlocation;
        Random random = new Random();
        int speedboostrandom = random.nextInt(5);
        switch (speedboostrandom){
                case 0:
                    spawnlocation = new Location(world, 171.5, 82, 302.5);
                    break;
                case 1:
                    spawnlocation = new Location(world, 161.5, 82, 297.5);
                    break;
                case 2:
                    spawnlocation = new Location(world, 157.5, 82, 301.5);
                    break;
                case 3:
                    spawnlocation = new Location(world, 152.5, 83, 309.5);
                    break;
                case 4:
                    spawnlocation = new Location(world, 177.5, 83, 317.5);
                    break;
                case 5:
                    spawnlocation = new Location(world, 164.5, 82, 312.5);
                    break;
                default:
                    spawnlocation = new Location(world, 164.5, 82, 312.5);
                    break;
            }
            return spawnlocation;
        }
    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent e){
        Player p = e.getPlayer();
        Item item = e.getItem();
        PotionEffect speedeffect = new PotionEffect(PotionEffectType.SPEED, 20*15,2);
        item.setItemStack(new ItemStack(Material.AIR));
        item.getLocation().setPitch(0.0F);
        item.getLocation().setYaw(0.0F);
        item.teleport(new Location(world, item.getLocation().getX(),
                item.getLocation().getY(), item.getLocation().getZ(), 0.0F, 0.0F));
        e.setCancelled(true);
        item.remove();
        p.addPotionEffect(speedeffect);
        ArmorStand astoremove = SpeedBoostSpawner.armorstands.get(e.getItem().getLocation());
        astoremove.remove();
        SpeedBoostSpawner.armorstands.remove(e.getItem().getLocation());
    }
}
