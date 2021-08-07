package fr.opoc.duel;

import fr.opoc.duel.arenas.ArenaManager;
import fr.opoc.duel.commands.Commands;
import fr.opoc.duel.files.ArenaFile;
import fr.opoc.duel.managers.RequestManager;
import org.bukkit.plugin.java.JavaPlugin;



public class DuelPlugin extends JavaPlugin {

    private static DuelPlugin instance;
    private RequestManager request;
    private ArenaManager arenaManager;
    private ArenaFile arenaFile;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        instance=this;
        this.request = new RequestManager(this);
        this.arenaManager = new ArenaManager(this);
        this.arenaFile = new ArenaFile(this);
        this.arenaFile.setup();
        this.getCommand("duel").setExecutor(new Commands(this));
        this.arenaManager.loadArenas();
    }

    @Override
    public void onDisable() {

    }

    public static DuelPlugin getInstance() {
        return instance;
    }

    public RequestManager getRequest(){
        return this.request;
    }
    public ArenaManager getArenaManager() {
        return this.arenaManager;
    }
    public ArenaFile getArenaFile() {
        return this.arenaFile;
    }
}