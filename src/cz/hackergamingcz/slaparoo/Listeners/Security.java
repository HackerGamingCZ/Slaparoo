package cz.hackergamingcz.slaparoo.Listeners;

import cz.hackergamingcz.slaparoo.Handlers.Game;
import cz.hackergamingcz.slaparoo.Handlers.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Security implements Listener{
    //U hitu hráče nastaví full health oběti
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e){
        Player player = (Player) e.getEntity();
        player.setHealth(20);
        if(GameState.isState(GameState.RESET) || GameState.isState(GameState.WAITING)){
            e.setCancelled(true);
        }
    }

    //Ukončuje jakýkoliv interakt event
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        e.setCancelled(true);
    }
    //Zabrání jakémukoliv klikání v inventáři
    @EventHandler
    public void inventoryInteract(InventoryClickEvent e){
            e.getWhoClicked().closeInventory();
            e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onItemSwitchHand(PlayerSwapHandItemsEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onSwitchHeldItem(PlayerItemHeldEvent e){
        e.setCancelled(true);
    }
}
