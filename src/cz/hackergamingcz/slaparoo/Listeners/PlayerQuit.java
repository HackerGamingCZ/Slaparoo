package cz.hackergamingcz.slaparoo.Listeners;

import cz.hackergamingcz.slaparoo.Handlers.Game;
import cz.hackergamingcz.slaparoo.Handlers.GameState;
import cz.hackergamingcz.slaparoo.SBManager;
import cz.hackergamingcz.slaparoo.Threads.LobbyCountdown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        e.setQuitMessage("");
        LobbyCountdown.sbm.remove(player);
        Bukkit.broadcastMessage("§aSlaparoo > §6"+player.getName()+" §ese odpojil ze hry!");
        if(GameState.getState() == GameState.WAITING){
            int score = KillCounter.score.get(player);
            KillCounter.score.remove(player);
            KillCounter.scoreconversely.remove(score,player);
            if(Bukkit.getOnlinePlayers().size()-1 < 2 && LobbyCountdown.isCountdownRunning() ){
                LobbyCountdown.reset();
                LobbyCountdown.stop();
                LobbyCountdown.sbm.forEach((nick, sbm) -> sbm.updateScoreboard(Bukkit.getOnlinePlayers().size()-1));
                for(Player p : Bukkit.getOnlinePlayers()){
                    p.setLevel(0);
                }
                Bukkit.broadcastMessage("§aSlaparoo > §cNedostatek hráčů pro start hry! Zastavuji odpočet..");
            }
        } else{
            if(Bukkit.getOnlinePlayers().size()-1-Game.spectators.size() < 2 && GameState.isState(GameState.INGAME)){
                Game.end();
            }
        }
    }

}
