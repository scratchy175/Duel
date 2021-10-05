package fr.opoc.duel.arenas;

import fr.opoc.duel.DuelPlugin;
import fr.opoc.duel.files.ArenaFile;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class ArenaManager {

    private DuelPlugin plugin;
    private ArrayList<Arena> arenas = new ArrayList<>();

    public ArenaManager(DuelPlugin plugin) {
        this.plugin=plugin;

    }

    public void addArena(String name){
        Arena arena = new Arena(this.plugin,name);
        this.arenas.add(arena);
    }

    public void addToArena(Player firstPlayer,Player secondPlayer){
        Arena nextArena = getNextArena();
        if (nextArena!=null){
            nextArena.getPlayers().add(firstPlayer);
            nextArena.getPlayers().add(secondPlayer);
            firstPlayer.teleport(nextArena.getFirstLocation());
            secondPlayer.teleport(nextArena.getSecondLocation());
            nextArena.setState(ArenaState.COUNTDOWN);
            nextArena.startCountdown();
        }
        else {
            firstPlayer.sendMessage("il n'y a pas d'arene disponible.");
            secondPlayer.sendMessage("il n'y a pas d'arene disponible.");
        }
    }

    public Arena getNextArena(){
        for (Arena arena : arenas){
            ArenaFile arenaFile = plugin.getArenaFile();
            if (arena.isAvailable()){

                return arena;
            }
        }
        return null;
    }
    public ArrayList<Arena> getArena(){
        return arenas;
    }


    public Arena getArena(Player player) {
        for (Arena arena : arenas){
            if (arena.getPlayers().contains(player)){
                return arena;
            }

        }
        return null;
    }

    public void loadArenas(){
        for (String name : this.plugin.getArenaFile().getArenaconfig().getConfigurationSection("arenas").getKeys(false)){
            addArena(name);
        }
        System.out.println("Arenas loaded : " + arenas.size());
    }
}
