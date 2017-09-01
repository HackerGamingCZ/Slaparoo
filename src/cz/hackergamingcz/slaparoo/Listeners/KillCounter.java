package cz.hackergamingcz.slaparoo.Listeners;

import cz.hackergamingcz.slaparoo.Handlers.Game;
import cz.hackergamingcz.slaparoo.Handlers.GameState;
import cz.hackergamingcz.slaparoo.Mechanics;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class KillCounter implements Listener {

    //<Victim, Damager>
    private HashMap<Player, Player> hitLog = new HashMap<>();
    public static HashMap<Player, Integer> score = new HashMap<>();
    public static HashMap<Integer, Player> scoreconversely = new HashMap<>();

    @EventHandler
    public void onPlayerHitsPlayer(EntityDamageByEntityEvent e){
        Player victim = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();
        hitLog.put(victim, damager);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        player.setPlayerWeather(WeatherType.CLEAR);
        player.setPlayerTime(12L, true);
        player.setFoodLevel(20); //dá hungrybar hráče na max., pokud se pohne
        player.setFallDistance(0);
        if(player.getLocation().getY() <= 50){
            if(GameState.isState(GameState.RESET)){
                player.teleport(Game.endlobby);
            } else{
                player.teleport(Game.randomRespawn());
            }
            if(hitLog.containsKey(player)){
                int killerscore = score.get(hitLog.get(player))+1;
                Player killer = hitLog.get(player);
                killer.setPlayerListName(killer.getName() + "§e - "+killerscore);
                score.put(hitLog.get(player), killerscore);
                scoreconversely.put(killerscore, hitLog.get(player));
                Bukkit.broadcastMessage("§aSlaparoo > §eHráč §6"+killer.getName()+" §eshodil hráče §6"+e.getPlayer().getName()+"§e!");
                Mechanics.playAnvillandSound(killer);
                hitLog.remove(player);
                if(killerscore >= 25){
                    Game.end();
                    return;
                }
            } else{
                Bukkit.broadcastMessage("§aSlaparoo > §eHráč §6"+e.getPlayer().getName()+" §espadl!");
            }
        }
    }
}
