package cz.hackergamingcz.slaparoo.Handlers;

import cz.hackergamingcz.slaparoo.Listeners.KillCounter;
import cz.hackergamingcz.slaparoo.Main;
import cz.hackergamingcz.slaparoo.Mechanics;
import cz.hackergamingcz.slaparoo.SBManager;
import cz.hackergamingcz.slaparoo.Threads.IngameCountdown;
import cz.hackergamingcz.slaparoo.Threads.LobbyCountdown;
import cz.hackergamingcz.slaparoo.Threads.SpeedBoostSpawner;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.StringUtil;

import java.util.HashMap;
import java.util.Random;

public class Game {

    private static World world = Bukkit.getWorld("world");
    public static Location winnerlocation = new Location(Bukkit.getWorld("world"), 79.5,64,319.5, 180.0F, 0.0F);
    public static Location endlobby = new Location(Bukkit.getWorld("world"), 79.5,64,315.5);
    public static HashMap<Player, Player> spectators = new HashMap<>();


    public static Location randomRespawn(){
        Location respawnLocation;
        Random randomnumber = new Random();
        int spawnid = randomnumber.nextInt(4);
        switch (spawnid){
            case 0:
                respawnLocation = new Location(world, 170, 82, 300);
                break;
            case 1:
                respawnLocation = new Location(world, 173, 82, 302);
                break;
            case 2:
                respawnLocation = new Location(world, 152, 83, 309);
                break;
            case 3:
                respawnLocation = new Location(world, 148, 82, 299);
                break;
            case 4:
                respawnLocation = new Location(world, 159, 82, 300);
                break;
            default:
                respawnLocation = new Location(world, 159, 82, 300);
                break;
        }
        return respawnLocation;
    }

    public static void start(){
        Mechanics.clearChat();
        Bukkit.broadcastMessage("§a§lSlaparoo > §e§lHra začíná!");
        GameState.setState(GameState.INGAME);
        SpeedBoostSpawner.start();
        LobbyCountdown.sbm = new HashMap<>();

        for(Player p : Bukkit.getOnlinePlayers()){
            LobbyCountdown.sbm.put(p, new SBManager(p));
        }
        IngameCountdown.start();
        World world = Bukkit.getWorld("world");
        Location ingamestartlocation = new Location(world, 165, 83, 305);
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.teleport(ingamestartlocation);
            Mechanics.giveCookie(p);
        }
    }

    public static void end(){
        Reset.start();
        for(ArmorStand as : SpeedBoostSpawner.armorstands.values()){
            as.remove();
        }
        SpeedBoostSpawner.stop();
        IngameCountdown.stop();
        Player winner = getWinner();
        for(Player p : Bukkit.getOnlinePlayers()){
            p.teleport(endlobby);
        }
        LobbyCountdown.sbm = new HashMap<>();


        for(Player p : Bukkit.getOnlinePlayers()){
            LobbyCountdown.sbm.put(p, new SBManager(p));
        }

        winner.teleport(winnerlocation);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            @Override
            public void run() {
                Firework f = (Firework) winner.getWorld().spawn(winner.getLocation().add(0,2.5,0), Firework.class);
                Random random = new Random();
                int RandomPower = random.nextInt(2);
                FireworkMeta fm = f.getFireworkMeta();
                fm.addEffect(FireworkEffect.builder()
                        .flicker(false)
                        .trail(true)
                        .with(FireworkEffect.Type.STAR)
                        .withColor(Color.YELLOW)
                        .build());
                fm.setPower(RandomPower);
                f.setFireworkMeta(fm);
            }
        }, 0L, 30L);
        String messageborder = "";
        int winnerscore = KillCounter.score.get(winner);
        for(int i = 0; i <= 21; i++){
            messageborder += "§e§l-§6§l-";
        }
        String winnertext = Mechanics.centerText("§e§lVyhrál hráč §6§l"+winner.getName()+" §e§ls §6§l"+winnerscore+" §e§lzabitími!");
        messageborder += "§e§l-";
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(messageborder);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(Mechanics.centerText("§e§lKonec hry!"));
        Bukkit.broadcastMessage(winnertext);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(messageborder);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§e§lVýsledky hráčů");
        for(Player p : KillCounter.score.keySet()){
            if(p != winner){
                Bukkit.broadcastMessage("§6§l"+p.getName()+" §e§l - §6§l"+KillCounter.score.get(p)+" §e§lzabití!");
            }
        }

    }
    public static Player getWinner(){
        int winnerscore = 0;
        for(int i : KillCounter.score.values()){
            if(i > winnerscore){
                winnerscore = i;
            }
        }
        Player winner = KillCounter.scoreconversely.get(winnerscore);
        return winner;
    }
}
