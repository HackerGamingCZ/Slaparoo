package cz.hackergamingcz.slaparoo.Threads;

import cz.hackergamingcz.slaparoo.Handlers.SpeedBoost;
import cz.hackergamingcz.slaparoo.Main;
import cz.hackergamingcz.slaparoo.Mechanics;
import org.bukkit.*;
import org.bukkit.util.Vector;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SpeedBoostSpawner {
    private static int countdownTaskId = -1;
    private static World world = Bukkit.getWorld("world");
    public static HashMap<Location, ArmorStand> armorstands = new HashMap<>();

    //Metoda sloužící k zjištění, jestli countdown již běží nebo ne
    public static boolean isCountdownRunning(){
        if(countdownTaskId != -1){
            return true;
        } else{
            return false;
        }
    }
    public static void start(){
        countdownTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () ->
        {
            ItemStack isfeather = new ItemStack(Material.FEATHER);
            Location spawn =  SpeedBoost.randomSpawn();
            Item item = world.dropItem(spawn, isfeather);
            item.setVelocity(new Vector(0, 0, 0));
            Entity entity = world.spawnEntity(spawn, EntityType.ARMOR_STAND);
            ArmorStand as = (ArmorStand) entity;
            as.setCustomName("§b§lRYCHLOST");
            as.setCustomNameVisible(true);
            as.setSmall(true);
            as.setVisible(false);
            armorstands.put(spawn, as);
        }, 600, 600);
    }
    public static void stop(){
        if(countdownTaskId != -1){
            Bukkit.getScheduler().cancelTask(countdownTaskId);
            countdownTaskId = -1;
        }
    }
}
