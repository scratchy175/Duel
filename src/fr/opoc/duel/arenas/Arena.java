package fr.opoc.duel.arenas;

import fr.opoc.duel.DuelPlugin;
import fr.opoc.duel.files.ArenaFile;
import fr.opoc.duel.files.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Arena {

    private DuelPlugin plugin;
    ArenaManager arenaManager;
    ArenaFile arenaFile;
    ArenaState state;
    ConfigFile config;
    private String name;
    private ArrayList <Player> players= new ArrayList<>();

    public Arena (DuelPlugin plugin,String name){
        this.name=name;
        this.plugin=plugin;
        this.arenaManager=plugin.getArenaManager();
        this.arenaFile = plugin.getArenaFile();
        this.config = plugin.getConfigFile();
        this.state= ArenaState.FREE;
    }

    public Location getFirstLocation(){
        if (arenaFile.getArenaconfig().get("arenas." + name + ".1")!=null) {
            double x = arenaFile.getArenaconfig().getDouble("arenas." + name + ".1.x");
            double y = arenaFile.getArenaconfig().getDouble("arenas." + name + ".1.y");
            double z = arenaFile.getArenaconfig().getDouble("arenas." + name + ".1.z");
            float yaw = (float) arenaFile.getArenaconfig().getLong("arenas." + name + ".1.Yaw");
            float pitch = (float) arenaFile.getArenaconfig().getLong("arenas." + name + ".1.Pitch");

            return new Location(Bukkit.getWorld(arenaFile.getArenaconfig().getString("arenas." + name + ".1.world")), x, y, z, yaw, pitch);
        }
        else return null;
    }
    public Location getSecondLocation(){
        if (arenaFile.getArenaconfig().get("arenas." + name + ".2")!=null) {
            double x = arenaFile.getArenaconfig().getDouble("arenas." + name + ".2.x");
            double y = arenaFile.getArenaconfig().getDouble("arenas." + name + ".2.y");
            double z = arenaFile.getArenaconfig().getDouble("arenas." + name + ".2.z");
            float yaw = (float) arenaFile.getArenaconfig().getLong("arenas." + name + ".2.Yaw");
            float pitch = (float) arenaFile.getArenaconfig().getLong("arenas." + name + ".2.Pitch");

            return new Location(Bukkit.getWorld(arenaFile.getArenaconfig().getString("arenas." + name + ".2.world")), x, y, z, yaw, pitch);
        }
        else return null;
    }
    public ArrayList<Player>getPlayers(){
        return this.players;
    }

    public void startCountdown() {
        (new BukkitRunnable() {
            int counter = config.getStartCooldown();


            public void run() {
                if (this.counter != 0) {
                    for (Player player : Arena.this.players) {
                        player.setLevel(this.counter);
                        player.sendMessage("debut dans " + counter);
                        player.sendTitle("Debut dans " + counter, "", 10, 10, 10);
                    }
                }
                else {
                    for (Player player : Arena.this.players) {
                        player.setLevel(0);
                        player.sendMessage("c'est parti !");
                        player.sendTitle("§4C'est parti !","",10,10,10);
                        setState(ArenaState.PLAYING);
                    }
                    cancel();

                }
                this.counter--;
            }
        }).runTaskTimer(this.plugin, 20L, 20L);
    }
    public ArenaState getState() {
        return this.state;
    }

    public void setState(ArenaState state) {
        this.state = state;
    }

    public void eliminate(Player player){
        players.remove(player);
        endGame();
    }
    public void endGame(){
        if (players.size()==1){
            Player winner = players.get(0);
            winner.sendMessage("Vous avez gagné le duel.");
            players.clear();
            setState(ArenaState.FREE);
        }
    }
    public boolean isAvailable(){
        return (getFirstLocation()!=null && getSecondLocation()!=null && getState()==ArenaState.FREE);
    }


}
