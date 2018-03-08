package cz.hackergamingcz.slaparoo.Handlers;

import cz.hackergamingcz.slaparoo.Listeners.KillCounter;
import cz.hackergamingcz.slaparoo.Main;
import cz.hackergamingcz.slaparoo.Mechanics;
import cz.hackergamingcz.slaparoo.SBManager;
import cz.hackergamingcz.slaparoo.Threads.IngameCountdown;
import cz.hackergamingcz.slaparoo.Threads.LobbyCountdown;
import cz.hackergamingcz.slaparoo.Threads.SpeedBoostSpawner;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game {

    private Location winnerlocation;
    public Location endlobby;
    public static HashMap<Player, Player> spectators = new HashMap<>();
    private ArrayList<Location> respawns = new ArrayList<>();

    public Game(){
        Configuration c = Main.plugin.getConfig();
        for(int i = 1; i <= Main.plugin.getConfig().getConfigurationSection("respawnlocs").getKeys(false).size(); i++)
        {
            respawns.add(new Location(Main.getArena(),
                    c.getDouble("respawnlocs." + i + ".loc.x"), c.getDouble("respawnlocs." + i + ".loc.y"),
                    c.getDouble("respawnlocs." + i + ".loc.z")));
        }
        endlobby = new Location(Main.getArena(), c.getDouble("endlobby.x"), c.getDouble("endlobby.y"), c.getDouble("endlobby.z"));
        winnerlocation = new Location(Main.getArena(), c.getDouble("winnerloc.x"), c.getDouble("winnerloc.y"), c.getDouble("winnerloc.z"));
    }


    public Location randomRespawn(){
        return respawns.get(Main.getRandom().nextInt(respawns.size()-1));
    }

    public void start(){
        Mechanics.clearChat();
        Bukkit.broadcastMessage(Main.getPrefix()+" §eHra začíná!");
        GameState.setState(GameState.INGAME);
        SpeedBoostSpawner.start();
        LobbyCountdown.sbm = new HashMap<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            LobbyCountdown.sbm.put(p, new SBManager(p));
        }
        IngameCountdown.start();
        Configuration c = Main.plugin.getConfig();
        Location ingamestartlocation = new Location(Main.getArena(), c.getDouble("game.x"), c.getDouble("game.y"), c.getDouble("game.z"));
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.teleport(ingamestartlocation);
            Mechanics.giveCookie(p);
        }
    }

    public void end(){
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
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, ()-> {
                Firework f = winner.getWorld().spawn(winner.getLocation().add(0,2.5,0), Firework.class);
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
    public Player getWinner(){
        int winnerscore = 0;
        for(int i : KillCounter.score.values()){
            if(i > winnerscore){
                winnerscore = i;
            }
        }
        return KillCounter.scoreconversely.get(winnerscore);
    }
}
