package cz.hackergamingcz.slaparoo.Threads;

import cz.hackergamingcz.slaparoo.Handlers.Game;
import cz.hackergamingcz.slaparoo.Main;
import cz.hackergamingcz.slaparoo.Mechanics;
import cz.hackergamingcz.slaparoo.SBManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ResetCountdown {

    private static int number = 15;
    private static int countdownTaskId = -1;

    public static int getNumber(){
        return number;
    }
    //Metoda sloužící k zjištění, jestli countdown již běží nebo ne
    public static boolean isCountdownRunning(){
        if(countdownTaskId != -1){
            return true;
        } else{
            return false;
        }
    }
    //Zahájí odpočítávání v lobby
    public static void start(){
        countdownTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () ->
        {
            number--;
            LobbyCountdown.sbm.forEach((nick, sbm) -> sbm.updateScoreboard());
            for(Player p : Bukkit.getOnlinePlayers()){
                p.setLevel(number);
            }
            if(number <= 5){
                Bukkit.broadcastMessage("§aSlaparoo > §eDo restartu serveru zbývá §6"+number+" §esekund!");
                for(Player p : Bukkit.getOnlinePlayers())
                    Mechanics.playXpSound(p);
            }
            if(number < 1){
                Bukkit.getServer().shutdown();
            }
        }, 0, 20);
    }

}
