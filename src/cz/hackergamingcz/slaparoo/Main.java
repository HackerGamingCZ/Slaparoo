package cz.hackergamingcz.slaparoo;

import cz.hackergamingcz.slaparoo.Commands.ForceStart;
import cz.hackergamingcz.slaparoo.Commands.SetScore;
import cz.hackergamingcz.slaparoo.Commands.StopGame;
import cz.hackergamingcz.slaparoo.Commands.StopStart;
import cz.hackergamingcz.slaparoo.Handlers.Game;
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
import java.util.Random;

public class Main extends JavaPlugin {

    public static Main plugin;
    private static String prefix = "§8[§eSlaparoo§8]";
    private static World arena;
    private static Random random;
    private static Game game;

    @Override
    public void onEnable(){
        GameState.setState(GameState.WAITING);
        plugin = this;
        registerEvents();
        registerCommands();
        log("Plugin successfully started!");
        clearGround();
        arena = Bukkit.getWorld(getConfig().getConfigurationSection("general").getString("arena"));
        if(arena == null){
            GameState.setState(GameState.SETUP);
            log("Game is not ");
            return;
        }
        arena.setDifficulty(Difficulty.PEACEFUL);
        random = new Random();
        game = new Game();
    }

    public void log(String message){
        Bukkit.getLogger().info("[Slaparoo] "+message);
    }

    @Override
    public void onDisable(){
        log("Plugin successfully stopped!");
    }

    private void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new PlayerQuit(), this);
        pm.registerEvents(new Security(), this);
        pm.registerEvents(new KillCounter(), this);
        pm.registerEvents(new SpeedBoost(), this);
    }
    private  void registerCommands(){
        this.getCommand("forcestart").setExecutor(new ForceStart());
        this.getCommand("stopgame").setExecutor(new StopGame());
        this.getCommand("stopstart").setExecutor(new StopStart());

        //Just for testing
        this.getCommand("setscore").setExecutor(new SetScore());
    }

    private void clearGround(){
        World world = arena;
        List<Entity> entities = world.getEntities();

        for (Entity entity : entities) {
            if (entity instanceof Item || entity instanceof ArmorStand) {
                entity.remove();
            }
        }
    }

    public static String getPrefix() {
        return prefix;
    }

    public static World getArena() {
        return arena;
    }

    public static Random getRandom() {
        return random;
    }

    public static Game getGame() {
        return game;
    }
}
