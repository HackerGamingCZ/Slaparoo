package cz.hackergamingcz.slaparoo;

import cz.hackergamingcz.slaparoo.Commands.ForceStart;
import cz.hackergamingcz.slaparoo.Commands.SetScore;
import cz.hackergamingcz.slaparoo.Commands.StopGame;
import cz.hackergamingcz.slaparoo.Commands.StopStart;
import cz.hackergamingcz.slaparoo.Handlers.GameState;
import cz.hackergamingcz.slaparoo.Handlers.SpeedBoost;
import cz.hackergamingcz.slaparoo.Listeners.KillCounter;
import cz.hackergamingcz.slaparoo.Listeners.PlayerJoin;
import cz.hackergamingcz.slaparoo.Listeners.PlayerQuit;
import cz.hackergamingcz.slaparoo.Listeners.Security;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin {

    public static Main plugin;

    @Override
    public void onEnable(){
        GameState.setState(GameState.WAITING);
        plugin = this;
        registerEvents();
        registerCommands();
        System.out.println("[Slaparoo] plugin úspěšně zapnut.");
        clearGround();
        Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);
        }
    @Override
    public void onDisable(){
        System.out.println("[Slaparoo] plugin úspěšně vypnut.");
    }

    void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new PlayerQuit(), this);
        pm.registerEvents(new Security(), this);
        pm.registerEvents(new KillCounter(), this);
        pm.registerEvents(new SpeedBoost(), this);
    }
    void registerCommands(){
        this.getCommand("forcestart").setExecutor(new ForceStart());
        this.getCommand("stopgame").setExecutor(new StopGame());
        this.getCommand("stopstart").setExecutor(new StopStart());
        this.getCommand("setscore").setExecutor(new SetScore());
    }

    void clearGround(){
        World world = getServer().getWorld("world");
        List<Entity> entList = world.getEntities();

        for (Entity current : entList) {
            if (current instanceof Item || current instanceof ArmorStand) {
                current.remove();
            }
        }
    }

}
