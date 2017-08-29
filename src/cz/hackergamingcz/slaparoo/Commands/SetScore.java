package cz.hackergamingcz.slaparoo.Commands;

import cz.hackergamingcz.slaparoo.Handlers.Game;
import cz.hackergamingcz.slaparoo.Handlers.GameState;
import cz.hackergamingcz.slaparoo.Listeners.KillCounter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetScore implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player && GameState.isState(GameState.INGAME)){
            if(sender.isOp()){
                Player player = (Player) sender;
                KillCounter.score.remove(player);
                KillCounter.score.put(player, Integer.parseInt(args[0]));
                KillCounter.scoreconversely.remove(player);
                KillCounter.scoreconversely.put(Integer.parseInt(args[0]), player);
            } else{
                sender.sendMessage("§aSlaparoo > §cNemáš práva na tento příkaz!");
            }
        }
        return true;
    }
}