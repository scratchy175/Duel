package fr.opoc.duel.managers;

import fr.opoc.duel.DuelPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class RequestManager {
    private final DuelPlugin plugin;

    HashMap<UUID,UUID>requests = new HashMap<>();


    public RequestManager(DuelPlugin plugin){
        this.plugin=plugin;
    }

    public void addRequest (Player firstPlayer,Player secondPlayer){
        this.requests.put(firstPlayer.getUniqueId(),secondPlayer.getUniqueId());
    }
    public void removeRequest (Player player){
        this.requests.remove(player.getUniqueId());

    }
    public boolean hasRequest(Player player) {
        return this.requests.containsKey(player.getUniqueId());
    }
    public boolean hasRequestFrom(Player player, Player target) {
        return this.requests.get(player.getUniqueId()).equals(target.getUniqueId());
    }

}
