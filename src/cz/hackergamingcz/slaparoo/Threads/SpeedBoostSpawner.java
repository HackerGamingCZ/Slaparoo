package cz.hackergamingcz.slaparoo.Threads;

import cz.hackergamingcz.slaparoo.Handlers.SpeedBoost;
import cz.hackergamingcz.slaparoo.Main;
import cz.hackergamingcz.slaparoo.Mechanics;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.util.Vector;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SpeedBoostSpawner {
    private static int countdownTaskId = -1;
    public static HashMap<Location, ArmorStand> armorstands = new HashMap<>();
    private static SpeedBoost speedBoost = new SpeedBoost();


    //Metoda sloužící k zjištění, jestli countdown již běží nebo ne
    public static boolean isCountdownRunning(){
        return countdownTaskId != 1;
    }

    public static void start(){
        countdownTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () ->
        {
            ItemStack isfeather = new ItemStack(Material.FEATHER);
            Location spawn =  speedBoost.randomSpawn();
            Item item = Main.getArena().dropItem(spawn, isfeather);
            item.setVelocity(new Vector(0, 0, 0));
            Entity entity = Main.getArena().spawnEntity(spawn, EntityType.ARMOR_STAND);
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
