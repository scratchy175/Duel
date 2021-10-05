package fr.opoc.duel.files;

import fr.opoc.duel.DuelPlugin;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ArenaFile {
    private final DuelPlugin plugin;
    private File file;
    private FileConfiguration arenaconfig;

    public ArenaFile(DuelPlugin plugin) {
        this.plugin = plugin;
    }

    public void setup(){
        this.file= new File(this.plugin.getDataFolder(), "Arenas.yml");
        if (!this.file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.arenaconfig = new YamlConfiguration();
        try {
            this.arenaconfig.load(this.file);
        } catch (IOException |org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void save() {
        try {
            this.arenaconfig.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        save();
        this.arenaconfig = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getArenaconfig() {
        return this.arenaconfig;
    }
    public void setArenaLocation1(Location location,String name){
        getArenaconfig().set("arenas." + name + ".1.world",location.getWorld().getName());
        getArenaconfig().set("arenas." + name + ".1.x",location.getBlockX());
        getArenaconfig().set("arenas." + name + ".1.y",location.getBlockY());
        getArenaconfig().set("arenas." + name + ".1.z",location.getBlockZ());
        getArenaconfig().set("arenas." + name + ".1.Yaw",location.getYaw());
        getArenaconfig().set("arenas." + name + ".1.Pitch",location.getPitch());
    }
    public void setArenaLocation2(Location location,String name){
        getArenaconfig().set("arenas." + name + ".2.world",location.getWorld().getName());
        getArenaconfig().set("arenas." + name + ".2.x",location.getBlockX());
        getArenaconfig().set("arenas." + name + ".2.y",location.getBlockY());
        getArenaconfig().set("arenas." + name + ".2.z",location.getBlockZ());
        getArenaconfig().set("arenas." + name + ".2.Yaw",location.getYaw());
        getArenaconfig().set("arenas." + name + ".2.Pitch",location.getPitch());
    }

}
