package fr.opoc.duel.files;

import fr.opoc.duel.DuelPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigFile {
    DuelPlugin plugin;
    private final FileConfiguration fileConfiguration;
    private final int requestCooldown;
    private final int startCooldown;

    public ConfigFile(DuelPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.fileConfiguration = plugin.getConfig();
        this.requestCooldown = this.fileConfiguration.getInt("cooldown.request");
        this.startCooldown = this.fileConfiguration.getInt("coodown.start");

    }
    public int getRequestCooldown(){
        return this.requestCooldown;
    }

    public int getStartCooldown() {
        return this.startCooldown;
    }
}
