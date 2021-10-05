package fr.opoc.duel.managers;

import fr.opoc.duel.DuelPlugin;
import org.bukkit.entity.Player;

import java.util.*;


public class RequestManager {
    private final DuelPlugin plugin;

    Map<UUID,Set<UUID>> requests = new HashMap<>();
    


    public RequestManager(DuelPlugin plugin){
        this.plugin=plugin;
    }

    public void addRequest (Player player,Player target){
        Set<UUID> playerRequest = requests.get(player.getUniqueId());

        if (playerRequest == null) {
            playerRequest = new HashSet<>();
            playerRequest.add(target.getUniqueId());
            this.requests.put(player.getUniqueId(), playerRequest);
            System.out.println(requests.get(player.getUniqueId()));
            System.out.println(playerRequest);
            System.out.println("Bite2");
        } else {
            playerRequest.add(target.getUniqueId());
            System.out.println(requests.get(player.getUniqueId()));
            System.out.println(playerRequest);
            System.out.println("Bite");
        }
    }
    public void removeRequest (Player player,Player target, boolean all){
        Set<UUID> playerRequest = requests.get(player.getUniqueId());
        System.out.println("Remove1");
        System.out.println(requests.get(player.getUniqueId()));
        //System.out.println(playerRequest);
        System.out.println("Remove1");
        if ( playerRequest==null){
            return;
        }
        else if (all){
            playerRequest.clear();
            this.requests.remove(player.getUniqueId());
            System.out.println("Remove2");
            System.out.println(requests.get(player.getUniqueId()));
            //System.out.println(playerRequest);


        }
        else {
            playerRequest.remove(target.getUniqueId());
            System.out.println("Remove3");
            System.out.println(requests.get(player.getUniqueId()));
            //System.out.println(playerRequest);


        }

    }
    public boolean hasRequest(Player player) {
        return this.requests.containsKey(player.getUniqueId());
    }
    public boolean hasRequestFrom(Player player, Player target) {
        Set<UUID> playerRequest = requests.get(player.getUniqueId());
        if (playerRequest==null){
            return false;
        }
        else{
            return this.requests.get(player.getUniqueId()).contains(target.getUniqueId());
        }

    }

}
