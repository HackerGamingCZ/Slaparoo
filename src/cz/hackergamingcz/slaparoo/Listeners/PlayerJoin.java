package cz.hackergamingcz.slaparoo.Listeners;

import cz.hackergamingcz.slaparoo.Handlers.Game;
import cz.hackergamingcz.slaparoo.Handlers.GameState;
import cz.hackergamingcz.slaparoo.Main;
import cz.hackergamingcz.slaparoo.Mechanics;
import cz.hackergamingcz.slaparoo.SBManager;
import cz.hackergamingcz.slaparoo.Threads.LobbyCountdown;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if(GameState.isState(GameState.WAITING)) {
            KillCounter.score.put(player, 0);
            KillCounter.scoreconversely.put(0, player);
        } else{
            Game.spectators.put(player, player);
        }
        Mechanics.resetPlayer(player);
        LobbyCountdown.sbm.put(player, new SBManager(player));
        World world = Bukkit.getWorld("world");
        Location jointplocation = new Location(world, 219, 64, 338);
        player.teleport(jointplocation);
        e.setJoinMessage("");
        if(GameState.isState(GameState.INGAME)){
            player.setGameMode(GameMode.SPECTATOR);
            Location spectatortplocation = new Location(world, 165, 97, 305);
            player.teleport(spectatortplocation);
            Bukkit.broadcastMessage("§aSlaparoo > §6"+player.getName()+" §ese připojil do hry jako divák!");
        } else{
            player.setGameMode(GameMode.ADVENTURE);
            Bukkit.broadcastMessage("§aSlaparoo > §6"+player.getName()+" §ese připojil do hry!");
        }
        if(Bukkit.getOnlinePlayers().size() < 2){
            Bukkit.broadcastMessage("§aSlaparoo > §cNedostatek hráčů pro start! Pro start jsou potřeba alespoň 2 hráči!");
        } else{
            if(GameState.isState(GameState.WAITING)){
                if(!LobbyCountdown.isCountdownRunning()){
                    LobbyCountdown.start();
                }
                if(Bukkit.getOnlinePlayers().size() >= 2 && LobbyCountdown.getNumber() > 10){
                    LobbyCountdown.setNumber(10);
                }
            }
        }
    }

}
