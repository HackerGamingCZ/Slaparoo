package cz.hackergamingcz.slaparoo;

import cz.hackergamingcz.slaparoo.Listeners.KillCounter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Mechanics {
    //Dá knockback 5 sušenku s názvem Rukes
    public static void giveCookie(Player player){
    ItemStack cookie = new ItemStack(Material.COOKIE, 1);
    ItemMeta meta = cookie.getItemMeta();
    meta.setDisplayName("§aRukes");
    cookie.setItemMeta(meta);
    cookie.addUnsafeEnchantment(Enchantment.KNOCKBACK, 6);
    player.getInventory().setItem(4, cookie);
    player.getInventory().setHeldItemSlot(4);
    }

    //Spustí zvuk sebrání XP orbu pro určitého hráče
    public static void playXpSound(Player player){
        player.playSound(player.getEyeLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
    }
    //Spustí zvuk exploze pro určitého hráče
    public static void playAnvillandSound(Player player){
        player.playSound(player.getEyeLocation(), Sound.BLOCK_ANVIL_LAND, 1F, 1F);
    }
    //Nastaví vše na default hodnoty (health, foodlevel, armor, atd..)
    public static void resetPlayer(Player p){
        p.setGameMode(GameMode.SURVIVAL);
        p.setFoodLevel(20);
        p.setHealth(20);
        p.setLevel(0);
        p.getInventory().clear();
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
    }
    public static String centerText(String text) {
        int maxWidth = 80,
                spaces = (int) Math.round((maxWidth-1.4*ChatColor.stripColor(text).length())/2);
        return StringUtils.repeat(" ", spaces)+text;
    }
    public static void clearChat(){
        for(int i = 0; i <= 20; i++){
            Bukkit.broadcastMessage("");
        }
    }
    public static String secondsToTime(double seconds){
        String time = "";
        double minutes = Math.floor(seconds/60);
        seconds = seconds-(minutes*60);
        if(seconds < 10){
            time = (int)minutes+":"+"0"+(int)seconds;
        } else{
            time = (int)minutes+":"+(int)seconds;
        }
        return time;
    }
    public static Player getTopPlayer(){
        int bestscore = 0;
        for(int i : KillCounter.score.values()){
            if(i > bestscore){
                bestscore = i;
            }
        }
        Player topplayer = KillCounter.scoreconversely.get(bestscore);
        return topplayer;
    }
    public static int getTopScore(){
        int bestscore = 0;
        for(int i : KillCounter.score.values()){
            if(i > bestscore){
                bestscore = i;
            }
        }
        return bestscore;
    }
}
