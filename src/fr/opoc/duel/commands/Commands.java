package fr.opoc.duel.commands;

import fr.opoc.duel.DuelPlugin;
import fr.opoc.duel.arenas.ArenaManager;
import fr.opoc.duel.files.ArenaFile;
import fr.opoc.duel.managers.RequestManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Commands implements CommandExecutor {
    DuelPlugin plugin;
    RequestManager request;
    ArenaManager arenaManager;
    ArenaFile arenaFile;
    private int cooldown;
    Map<UUID,Long> cooldowns = new HashMap<>();


    public Commands(DuelPlugin plugin) {
        this.plugin=plugin;
        this.request= plugin.getRequest();
        this.cooldown = 20;
        this.arenaManager=plugin.getArenaManager();
        this.arenaFile = plugin.getArenaFile();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("duel")){
            if (!(sender instanceof Player)){
                return false;
            }
            Player player = (Player)sender;
            if (args.length==0){
                player.sendMessage("/duel <pseudo>");
                player.sendMessage("/duel deny <pseudo>");
                player.sendMessage("/duel accept <pseudo>");
                return true;
            }
            if (args.length==1 && (Bukkit.getPlayer(args[0])!=null)){
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (!player.equals(target)) {
                        if (!this.request.hasRequest(player)){
                            long expiration = cooldown;
                            this.request.addRequest(player,target);
                            cooldowns.put(player.getUniqueId(),System.currentTimeMillis());
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                                this.request.removeRequest(player);
                                cooldowns.remove(player.getUniqueId());
                            }, expiration*20L);

                            player.sendMessage("Vous avez envoyé une demande en duel à " + target.getDisplayName());
                            player.sendMessage("Cette demande expirera dans " + cooldown + " secondes.");
                            target.sendMessage(player.getDisplayName() + " vous a fait une demande en duel.");
                            target.sendMessage("Cette demande expirera dans " + cooldown + " secondes.");
                        }

                        else {
                            long TimeLeft = cooldown - (System.currentTimeMillis()-cooldowns.get(player.getUniqueId()))/1000;
                            player.sendMessage("Vous avez deja une demande en attente.");
                            player.sendMessage("Vous devez attendre " + TimeLeft + " secondes avant de pouvoir renvoyer une nouvelle demande.");
                        }

                    }
                    else player.sendMessage("Vous ne pouvez pas défier vous meme en duel.");return true;
                }
                else player.sendMessage("Ce joueur n'existe pas");
            }
            /*else {
                player.sendMessage("/duel <pseudo>");
                player.sendMessage("/duel deny <pseudo>");
                player.sendMessage("/duel accept <pseudo>");
            }*/
            if (args.length==2){
                if (args[0].equalsIgnoreCase("accept")){
                    if (Bukkit.getPlayer(args[1]) != null) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (this.request.hasRequest(target) && this.request.hasRequestFrom(target, player)) {
                            this.request.removeRequest(target);
                            target.sendMessage(target.getDisplayName() + "à refusé votre demande en duel.");
                            player.sendMessage("Vous avez refusé la demande en duel de " + player.getDisplayName());
                            //start match
                        }
                        else player.sendMessage("Le joueur ne vous a pas envoyé de demande.");
                    }
                    else player.sendMessage("Le joueur est hors-ligne.");

                }

                else if (args[0].equalsIgnoreCase("deny")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (this.request.hasRequest(target) && this.request.hasRequestFrom(target, player)) {
                            this.request.removeRequest(target);
                            target.sendMessage(target.getDisplayName() + "à refusé votre demande en duel.");
                            player.sendMessage("Vous avez refusé la demande en duel de " + player.getDisplayName());
                        }
                        else player.sendMessage("Le joueur ne vous a pas envoyé de demande.");
                    }
                    else player.sendMessage("Le joueur est hors-ligne.");
                }
                //else player.sendMessage("Commande inconnu");
                else if (args[0].equalsIgnoreCase("createarena")){
                    if (player.hasPermission("duel.admin")){
                        String arenaName = args[1];
                        arenaManager.addArena(arenaName);
                        System.out.println(arenaManager.getArena());
                        arenaFile.getArenaconfig().createSection("arenas." + arenaName);
                        arenaFile.save();
                        player.sendMessage("L'arène " + arenaName + " a eté crée avec succès.");
                        player.sendMessage("Pour définir les points de spawn, utiliser la commande /duel setspawn " + arenaName + " 1.");

                    }
                }
            }
                else if (args.length==3){
                    if (args[0].equalsIgnoreCase("setspawn")){
                        String arenaName = args[1];
                        if (arenaFile.getArenaconfig().get("arenas")!=null) {
                            if (arenaFile.getArenaconfig().getConfigurationSection("arenas.").contains(arenaName)) {
                                if (args[2].equalsIgnoreCase("1")) {
                                    arenaFile.setArenaLocation1(player.getLocation(),arenaName);
                                    arenaFile.save();
                                    player.sendMessage("Position 1 défini pour l'arène " + arenaName + " ("+ player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + ").");
                                } else if (args[2].equalsIgnoreCase("2")) {
                                    arenaFile.setArenaLocation2(player.getLocation(),arenaName);
                                    arenaFile.save();
                                    player.sendMessage("Position 2 défini pour l'arène " + arenaName + " ("+ player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + ").");
                                }
                            }
                            else player.sendMessage("L'arène n'éxiste pas.");

                        }
                        else player.sendMessage("L'arène n'éxiste pas.");


                    }
                    else player.sendMessage("Commande inconnu.");
                }



        }
        return true;

    }
}