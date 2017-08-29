package cz.hackergamingcz.slaparoo.Commands;

import cz.hackergamingcz.slaparoo.Handlers.GameState;
import cz.hackergamingcz.slaparoo.Threads.LobbyCountdown;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player && GameState.isState(GameState.WAITING)){
            if(sender.isOp()){
                Bukkit.broadcastMessage("§aSlaparoo > §6"+sender.getName()+"§e vynutil start hry.");
                if(!LobbyCountdown.isCountdownRunning()){
                    LobbyCountdown.start();
                }
                LobbyCountdown.setNumber(6);
            } else{
                sender.sendMessage("§aSlaparoo > §cNemáš práva na rychlý start hry!");
            }
        } else{
            sender.sendMessage("§aSlaparoo > §cHra je již spuštěna!");
        }
        return true;
    }

}
