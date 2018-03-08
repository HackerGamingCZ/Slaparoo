package cz.hackergamingcz.slaparoo.Handlers;

import cz.hackergamingcz.slaparoo.Main;
import cz.hackergamingcz.slaparoo.Threads.SpeedBoostSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SpeedBoost implements Listener{
    private ArrayList<Location> spawns = new ArrayList<>();

    public SpeedBoost(){
        Configuration c = Main.plugin.getConfig();
        for(int i = 1; i <= Main.plugin.getConfig().getConfigurationSection("respawnlocs").getKeys(false).size(); i++)
        {
            spawns.add(new Location(Main.getArena(),
                    c.getDouble("gadget." + i + ".loc.x"), c.getDouble("gadget." + i + ".loc.y"),
                    c.getDouble("gadget." + i + ".loc.z")));
        }
    }

    public Location randomSpawn(){
        return spawns.get(Main.getRandom().nextInt(spawns.size()-1));
    }

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent e){
        Player p = e.getPlayer();
        Item item = e.getItem();
        PotionEffect speedeffect = new PotionEffect(PotionEffectType.SPEED, 20*15,2);
        item.setItemStack(new ItemStack(Material.AIR));
        item.getLocation().setPitch(0.0F);
        item.getLocation().setYaw(0.0F);
        item.teleport(new Location(e.getPlayer().getLocation().getWorld(), item.getLocation().getX(),
                item.getLocation().getY(), item.getLocation().getZ(), 0.0F, 0.0F));
        e.setCancelled(true);
        item.remove();
        p.addPotionEffect(speedeffect);
        ArmorStand astoremove = SpeedBoostSpawner.armorstands.get(e.getItem().getLocation());
        astoremove.remove();
        SpeedBoostSpawner.armorstands.remove(e.getItem().getLocation());
    }
}
