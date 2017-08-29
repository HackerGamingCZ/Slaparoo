package cz.hackergamingcz.slaparoo.Threads;

import cz.hackergamingcz.slaparoo.Handlers.Game;
import cz.hackergamingcz.slaparoo.Main;
import cz.hackergamingcz.slaparoo.Mechanics;
import cz.hackergamingcz.slaparoo.SBManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LobbyCountdown {

    private static int number = 60;
    private static int countdownTaskId = -1;
    public static  Map<Player, SBManager> sbm = new HashMap<>();

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
            sbm.forEach((nick, sbm) -> sbm.updateScoreboard());
            for(Player p : Bukkit.getOnlinePlayers()){
                p.setLevel(number);
            }
            if((number > 10 && number % 10 == 0) || number < 11){
                Bukkit.broadcastMessage("§aSlaparoo > §eDo startu hry zbývá §6"+number+" §esekund!");
                for(Player p : Bukkit.getOnlinePlayers())
                Mechanics.playXpSound(p);
            }
            if(number < 1){
                stop();
                Game.start();
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
    public static void reset(){
        number = 60;
    }
    public static void setNumber(int numbertochange){
        number = numbertochange;
    }
}
