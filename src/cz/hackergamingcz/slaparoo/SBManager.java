package cz.hackergamingcz.slaparoo;

import cz.hackergamingcz.slaparoo.Handlers.Game;
import cz.hackergamingcz.slaparoo.Listeners.KillCounter;
import cz.hackergamingcz.slaparoo.Threads.IngameCountdown;
import cz.hackergamingcz.slaparoo.Threads.LobbyCountdown;
import cz.hackergamingcz.slaparoo.Threads.ResetCountdown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import cz.hackergamingcz.slaparoo.Handlers.GameState;

import java.util.concurrent.CountDownLatch;

public class SBManager {
    private Scoreboard board;
    private Objective obj;
    private Player player;
    private String playerdisplayname;

    public void updateScoreboard(){
        if(!Game.spectators.containsKey(player)) {
            switch (GameState.getState()) {
                case WAITING:
                    updateWaitingSb();
                    break;
                case INGAME:
                    updateIngameSb();
                    break;

                case RESET:
                    updateResetSb();
                    break;
            }
        } else{
            if(GameState.isState(GameState.INGAME)){
                updateSpectSb();
            } else{
                updateResetSb();
            }
        }
    }

    public void updateScoreboard(int onlineplayers){
        if(!Game.spectators.containsKey(player)) {
            switch (GameState.getState()){
                case WAITING:
                    updateWaitingSb(onlineplayers);
                    break;
                case INGAME:
                    updateIngameSb();
                    break;

                case RESET:
                    updateResetSb();
                    break;
            }
        } else{
            if(GameState.isState(GameState.INGAME)){
                updateSpectSb();
            } else{
                updateResetSb();
            }
        }

    }

    public SBManager(Player p) {
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        p.setScoreboard(board);
        player = p;
        playerdisplayname = p.getDisplayName();

        if(playerdisplayname.length() > 13){
            playerdisplayname = playerdisplayname.substring(0,13);
        }

        obj = board.registerNewObjective("main", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("§e§lSlaparoo");
        if(!Game.spectators.containsKey(player)) {
            if (GameState.getState() == GameState.WAITING) {
                createWaitingSb();
            }
            if (GameState.getState() == GameState.INGAME) {
                createIngameSb();
            }
            if (GameState.getState() == GameState.RESET) {
                createResetSb();
            }
        } else{
            if(GameState.isState(GameState.INGAME)){
                createSpectBoard();
            } else{
                createResetSb();
            }
        }
    }
    public void createIngameSb(){
        //návrh na scoreboard ve hře
        /*
                Slaparoo
                                8
         Konec za: 123          7
                                6
         Zabití: 10             5
                                4
         Top hráč:              3
         HackerGamingCZ         2
         15 zabití              1
                                0
         */
        obj.getScore("§2").setScore(8);
        Team d = board.registerNewTeam(playerdisplayname+"d");
        d.addPlayer(Bukkit.getServer().getOfflinePlayer(ChatColor.GREEN+""));
        d.setPrefix("§7Konec za: ");
        d.setSuffix("§a"+ Mechanics.secondsToTime(IngameCountdown.getNumber())+"§l");
        obj.getScore(ChatColor.GREEN+"").setScore(7);
        obj.getScore("§8").setScore(6);
        Team e = board.registerNewTeam(playerdisplayname+"e");
        e.addPlayer(Bukkit.getServer().getOfflinePlayer(ChatColor.DARK_PURPLE+""));
        e.setPrefix("§7Zabití: ");
        e.setSuffix("§a"+ KillCounter.score.get(player));
        obj.getScore(ChatColor.DARK_PURPLE+"").setScore(5);
        obj.getScore("§3").setScore(4);
        obj.getScore("§7Nejlepší hráč:").setScore(3);
        int bestscore = 0;
        for(int i : KillCounter.score.values()){
            if(i > bestscore){
                bestscore = i;
            }
        }
        Player topplayer = KillCounter.scoreconversely.get(bestscore);
        Team g = board.registerNewTeam(playerdisplayname+"g");
        g.addPlayer(Bukkit.getServer().getOfflinePlayer(ChatColor.BLACK+""+ChatColor.GREEN+""));
        g.setPrefix("§2");
        g.setSuffix(topplayer.getName());
        obj.getScore(ChatColor.BLACK+""+ChatColor.GREEN+"").setScore(2);
        Team h = board.registerNewTeam(playerdisplayname+"h");
        h.addPlayer(Bukkit.getServer().getOfflinePlayer(ChatColor.BOLD+""));
        h.setPrefix("§7Má §a"+bestscore+"");
        h.setSuffix(" §7zabití");
        obj.getScore(ChatColor.BOLD+"").setScore(1);
        obj.getScore("§4").setScore(0);
    }
    public void updateIngameSb(){
        if(board.getObjective("main") == null)
        {
            createIngameSb();
            return;
        }
        try
        {
            board.getTeam(playerdisplayname+"d").setSuffix("§a"+ Mechanics.secondsToTime(IngameCountdown.getNumber()));
            board.getTeam(playerdisplayname+"e").setSuffix("§a"+ KillCounter.score.get(player));
            int bestscore = 0;
            for(int i : KillCounter.score.values()){
                if(i > bestscore){
                    bestscore = i;
                }
            }
            Player topplayer = KillCounter.scoreconversely.get(bestscore);
            board.getTeam(playerdisplayname+"g").setSuffix(topplayer.getName());
            board.getTeam(playerdisplayname+"h").setPrefix("§7Má §a"+bestscore+"");
            }
        catch(NullPointerException e)
        {
            Bukkit.getLogger().warning("null pointer u updateIngameSb");
        }
    }

    private void createWaitingSb(){
        //návrh na scoreboard v lobby
        /*
                Slaparoo
                                4
         Start za: 60           3
                                2
         Hráčů: 10              1
                                0
         */
        obj.getScore("§a").setScore(4);
        Team a = board.registerNewTeam(playerdisplayname+"a");
        a.addPlayer(Bukkit.getServer().getOfflinePlayer(ChatColor.GRAY+""));
        a.setPrefix("§7Start za: ");
        a.setSuffix("§a"+Mechanics.secondsToTime(LobbyCountdown.getNumber()));
        obj.getScore(ChatColor.GRAY+"").setScore(3);
        obj.getScore("§c").setScore(2);
        Team b = board.registerNewTeam(playerdisplayname+"b");
        b.addPlayer(Bukkit.getServer().getOfflinePlayer(ChatColor.BLACK+""));
        b.setPrefix("§7Hráčů: ");
        b.setSuffix("§a"+Bukkit.getOnlinePlayers().size());
        obj.getScore(ChatColor.BLACK+"").setScore(1);
        obj.getScore(ChatColor.DARK_PURPLE+"").setScore(0);
    }

    public void updateWaitingSb(){
        if(board.getObjective("main") == null)
        {
            createWaitingSb();
            return;
        }
        try
        {
            board.getTeam(playerdisplayname+"a").setSuffix("§a"+Mechanics.secondsToTime(LobbyCountdown.getNumber()));
            board.getTeam(playerdisplayname+"b").setSuffix("§a"+Bukkit.getOnlinePlayers().size());
        }
        catch(NullPointerException e)
        {
            Bukkit.getLogger().warning("null pointer u updateWaitingSb");
        }
    }
    public void updateWaitingSb(int onlineplayers){
        if(board.getObjective("main") == null)
        {
            createWaitingSb();
            return;
        }
        try
        {
            board.getTeam(playerdisplayname+"a").setSuffix("§a"+Mechanics.secondsToTime(LobbyCountdown.getNumber()));
            board.getTeam(playerdisplayname+"b").setSuffix("§a"+onlineplayers);
        }
        catch(NullPointerException e)
        {
            Bukkit.getLogger().warning("null pointer u updateWaitingSb");
        }
    }
    private void createResetSb(){
        //návrh na scoreboard v end lobby
        /*
                Slaparoo
                                8
         Reset za: 60           7
                                6
         Zabití: 25             5
                                4
         Výherce:               3
         HackerGamingCZ         2
         Gratulujeme!           1 //ukáže se jen pokud je hráč zároveň výhercem
                                0
         */
        obj.getScore("§a").setScore(8);
        Team i = board.registerNewTeam(playerdisplayname+"i");
        i.addPlayer(Bukkit.getServer().getOfflinePlayer(ChatColor.DARK_AQUA+""));
        i.setPrefix("§7Reset za: ");
        i.setSuffix("§a"+Mechanics.secondsToTime(ResetCountdown.getNumber()));
        obj.getScore(ChatColor.DARK_AQUA+"").setScore(7);
        obj.getScore("§5§l").setScore(6);
        Team j = board.registerNewTeam(playerdisplayname+"j");
        j.addPlayer(Bukkit.getServer().getOfflinePlayer(ChatColor.BLUE+""));
        j.setPrefix("§7Zabití: ");
        j.setSuffix("§a"+KillCounter.score.get(player));
        obj.getScore(ChatColor.BLUE+"").setScore(5);
        obj.getScore("§a§l").setScore(4);
        obj.getScore("§7Výherce: ").setScore(3);
        Team l = board.registerNewTeam(playerdisplayname+"l");
        l.addPlayer(Bukkit.getServer().getOfflinePlayer(ChatColor.YELLOW+""+ChatColor.GREEN+""));
        l.setPrefix("§4");
        l.setSuffix(Main.getGame().getWinner().getName());
        obj.getScore(ChatColor.YELLOW+""+ChatColor.GREEN+"").setScore(2);
        if(Main.getGame().getWinner().getName().equals(player.getName())){
            obj.getScore("§aGratulujeme!").setScore(1);
        }
        obj.getScore("§a§e").setScore(0);
    }
    public void updateResetSb(){
        if(board.getObjective("main") == null)
        {
            createWaitingSb();
            return;
        }
        try
        {
            board.getTeam(playerdisplayname+"i").setSuffix("§a"+ Mechanics.secondsToTime(ResetCountdown.getNumber()));
            board.getTeam(playerdisplayname+"j").setSuffix("§a"+ KillCounter.score.get(player));
            board.getTeam(playerdisplayname+"l").setSuffix(Main.getGame().getWinner().getName());
        }
        catch(NullPointerException e)
        {
            Bukkit.getLogger().warning("null pointer u updateWaitingSb");
        }
    }
    public void createSpectBoard(){
        /**
         *      Slaparoo
         *                      8
         * Jsi divák!           7
         *                      6
         * Konec za: 123        5
         *                      4
         * Nejlepší hráč:       3
         * HackerGamingCZ       2
         * Má 12 zabití         1
         *                      0
         */
        obj.getScore("§2§3").setScore(8);
        obj.getScore("§aJsi divák!").setScore(7);
        obj.getScore("§a§2").setScore(6);
        Team m = board.registerNewTeam(playerdisplayname+"m");
        m.addPlayer(Bukkit.getServer().getOfflinePlayer(ChatColor.RED+""+ChatColor.GOLD+""));
        m.setPrefix("§7Konec za: ");
        m.setSuffix("§a"+ Mechanics.secondsToTime(IngameCountdown.getNumber())+"§l");
        obj.getScore(ChatColor.RED+""+ChatColor.GOLD+"").setScore(5);
        obj.getScore("§a§4").setScore(4);
        obj.getScore("§7Nejlepší hráč:").setScore(3);
        Player topplayer = Mechanics.getTopPlayer();
        Team n = board.registerNewTeam(playerdisplayname+"n");
        n.addPlayer(Bukkit.getServer().getOfflinePlayer(ChatColor.GREEN+""+ChatColor.GREEN+""));
        n.setSuffix(topplayer.getName());
        obj.getScore(ChatColor.GREEN+""+ChatColor.GREEN+"").setScore(2);
        Team o = board.registerNewTeam(playerdisplayname+"o");
        o.addPlayer(Bukkit.getServer().getOfflinePlayer(ChatColor.BLACK+""+ChatColor.DARK_PURPLE+""+ChatColor.GREEN+""));
        o.setPrefix("§7Má ");
        o.setSuffix(Mechanics.getTopScore()+" §7zabití!");
        obj.getScore(ChatColor.BLACK+""+ChatColor.DARK_PURPLE+""+ChatColor.GREEN+"").setScore(1);
        obj.getScore("§a§4§l").setScore(0);
    }
    public void updateSpectSb(){
        if(board.getObjective("main") == null)
        {
            createWaitingSb();
            return;
        }
        try
        {
            board.getTeam(playerdisplayname+"m").setSuffix("§a"+ Mechanics.secondsToTime(IngameCountdown.getNumber())+"§l");
            board.getTeam(playerdisplayname+"n").setSuffix(Mechanics.getTopPlayer().getName());
            board.getTeam(playerdisplayname+"o").setSuffix(Mechanics.getTopScore()+" §7zabití!");
        }
        catch(NullPointerException e)
        {
            Bukkit.getLogger().warning("null pointer u updateWaitingSb");
        }
    }
}