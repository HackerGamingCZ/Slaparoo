package cz.hackergamingcz.slaparoo.Threads;

import cz.hackergamingcz.slaparoo.Handlers.Game;
import cz.hackergamingcz.slaparoo.Main;
import cz.hackergamingcz.slaparoo.Mechanics;
import cz.hackergamingcz.slaparoo.SBManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class IngameCountdown {
    private static int number = 240;
    private static int countdownTaskId = -1;

    public static void start(){
        countdownTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () ->
        {
            number--;
            for(Player p : Bukkit.getOnlinePlayers()){
                p.setLevel(number);
            }
            LobbyCountdown.sbm.forEach((nick, sbm) -> sbm.updateScoreboard());
            if((number > 10 && number % 100 == 0) || number < 11){
                Bukkit.broadcastMessage("§aSlaparoo > §eDo konce hry zbývá §6"+number+" §esekund!");
                for(Player p : Bukkit.getOnlinePlayers()){
                    Mechanics.playXpSound(p);
                }
            }
            if(number == 0){
                stop();
                Main.getGame().end();
            }
        }, 0, 20);
    }
    public static void stop(){
        if(countdownTaskId != -1){
            number = 60;
            Bukkit.getScheduler().cancelTask(countdownTaskId);
            countdownTaskId = -1;
        }
    }

    public static int getNumber(){
        return number;
    }
}
