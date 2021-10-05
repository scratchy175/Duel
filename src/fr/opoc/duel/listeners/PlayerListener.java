package fr.opoc.duel.listeners;

import fr.opoc.duel.DuelPlugin;
import fr.opoc.duel.arenas.Arena;
import fr.opoc.duel.arenas.ArenaState;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerListener implements Listener {

    DuelPlugin plugin;

    public PlayerListener (DuelPlugin plugin){
        this.plugin=plugin;

    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();
        Arena arena = plugin.getArenaManager().getArena(player);
        if (arena != null && arena.getPlayers().contains(player)) {
            if (arena.getState()==ArenaState.PLAYING) {
                arena.eliminate(player);
                //player.setHealth(20);
                //event.getDeathMessage();
                event.setDeathMessage(killer.getDisplayName() + "a tué" + player.getDisplayName());
                player.spigot().respawn();
            }
        }
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Arena arena = plugin.getArenaManager().getArena(player);
        if (arena!=null && arena.getPlayers().contains(player)){
            if (arena.getState()== ArenaState.COUNTDOWN){
                Location from = event.getFrom();
                Location to = event.getTo();
                if (from.getX() != to.getX() || from.getZ() != to.getZ() || from.getY() != to.getY())
                    event.setTo(event.getFrom());
            }
        }
    }
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Arena arena = plugin.getArenaManager().getArena(player);
        if (arena!=null && arena.getPlayers().contains(player)){
            arena.eliminate(player);
        }

    }
}
