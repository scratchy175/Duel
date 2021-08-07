package fr.opoc.duel.arenas;

import fr.opoc.duel.DuelPlugin;
import fr.opoc.duel.files.ArenaFile;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    private DuelPlugin plugin;
    ArenaFile arenaFile;
    ArenaFile af;
    private ArrayList<Arena> arenas = new ArrayList<>();

    public ArenaManager(DuelPlugin plugin) {
        this.plugin=plugin;
        //this.arenaFile = plugin.getArenaFile();
        this.af = plugin.getArenaFile();
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
        }
        else {
            firstPlayer.sendMessage("il n'y a pas d'arene disponible.");
            secondPlayer.sendMessage("il n'y a pas d'arene disponible.");
        }
    }

    private Arena getNextArena(){
        for (Arena arena : arenas){
            if (!arena.isStarted()){
                return arena;
            }

        }
        return null;
    }
    public ArrayList<Arena> getArena(){
        return arenas;
    }

    public void loadArenas(){
        for (String name : this.plugin.getArenaFile().getArenaconfig().getConfigurationSection("arenas").getKeys(false)){
            addArena(name);
        }
        System.out.println("Arenas loaded : " + arenas.size());
    }
}
