package cz.hackergamingcz.slaparoo.Handlers;

import cz.hackergamingcz.slaparoo.SBManager;
import cz.hackergamingcz.slaparoo.Threads.LobbyCountdown;
import cz.hackergamingcz.slaparoo.Threads.ResetCountdown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Reset {
    public static void start(){
        GameState.setState(GameState.RESET);
        LobbyCountdown.sbm = new HashMap<>();

        for(Player p : Bukkit.getOnlinePlayers()){
            LobbyCountdown.sbm.put(p, new SBManager(p));
            p.getInventory().clear();
        }
        ResetCountdown.start();
    }
}
