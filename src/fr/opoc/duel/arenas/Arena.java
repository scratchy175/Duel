package fr.opoc.duel.arenas;

import fr.opoc.duel.DuelPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Arena {

    private DuelPlugin plugin;
    private String name;
    private Location firstLocation;
    private Location secondLocation;
    private ArrayList <Player> players= new ArrayList<>();
    private boolean isStarted;

    public Arena (DuelPlugin plugin,String name){
        this.name=name;
        this.plugin=plugin;
        this.isStarted=false;
    }

    public Location getFirstLocation(){
        return this.firstLocation;
    }
    public Location getSecondLocation(){
        return this.secondLocation;
    }
    public ArrayList<Player>getPlayers(){
        return this.players;
    }
    public boolean isStarted(){
        return isStarted;
    }

}
