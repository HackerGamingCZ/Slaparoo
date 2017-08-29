package cz.hackergamingcz.slaparoo.Commands;

import cz.hackergamingcz.slaparoo.Handlers.Game;
import cz.hackergamingcz.slaparoo.Handlers.GameState;
import cz.hackergamingcz.slaparoo.Threads.LobbyCountdown;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopGame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player && GameState.isState(GameState.INGAME)){
            if(sender.isOp()){
                Bukkit.broadcastMessage("§aSlaparoo > §6"+sender.getName()+"§e vynutil ukončení hry.");
                Game.end();
            } else{
                sender.sendMessage("§aSlaparoo > §cNemáš práva na ukončený start hry!");
            }
        } else{
            sender.sendMessage("§aSlaparoo > §cHra ještě není spuštěna!");
        }
        return true;
    }
}
