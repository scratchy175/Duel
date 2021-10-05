package fr.opoc.duel.commands;

import fr.opoc.duel.DuelPlugin;
import fr.opoc.duel.arenas.Arena;
import fr.opoc.duel.arenas.ArenaManager;
import fr.opoc.duel.files.ArenaFile;
import fr.opoc.duel.files.ConfigFile;
import fr.opoc.duel.managers.ChatManager;
import fr.opoc.duel.managers.RequestManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor {
    DuelPlugin plugin;
    RequestManager request;
    ArenaManager arenaManager;
    ArenaFile arenaFile;
    ConfigFile config;
    ChatManager chatManager;


    public Commands(DuelPlugin plugin) {
        this.plugin=plugin;
        this.request= plugin.getRequest();
        this.arenaManager=plugin.getArenaManager();
        this.arenaFile = plugin.getArenaFile();
        this.chatManager = plugin.getChatManager();
        this.config = plugin.getConfigFile();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("duel")) {
            if (!(sender instanceof Player)) {
                return false;
            }
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage("/duel <pseudo>");
                player.sendMessage("/duel deny <pseudo>");
                player.sendMessage("/duel accept <pseudo>");
                return true;
            }
            else if (args.length == 1 /*&& (Bukkit.getPlayer(args[0]) != null)*/) {
                Player target = Bukkit.getPlayer(args[0]);
                Arena arena = plugin.getArenaManager().getArena(player);
                if (target != null) {
                    if (!player.equals(target)) {
                        if (arena==null || !arena.getPlayers().contains(player)) {
                            arena = plugin.getArenaManager().getArena(target);
                            if (arena==null || !arena.getPlayers().contains(target)) {
                                if (!this.request.hasRequestFrom(player, target)) {
                                    //long creation = System.currentTimeMillis();
                                    //if (System.currentTimeMillis()-creation/1000>=cooldown) {
                                    this.request.addRequest(player, target);
                                    Bukkit.getScheduler().runTaskLater(plugin, () -> this.request.removeRequest(player, target, false), config.getRequestCooldown() * 20L);
                                    player.sendMessage("Vous avez envoyé une demande en duel à " + target.getDisplayName());
                                    player.sendMessage("Cette demande expirera dans " + config.getRequestCooldown() + " secondes.");
                                    target.sendMessage(player.getDisplayName() + " vous a fait une demande en duel.");
                                    target.sendMessage("Cette demande expirera dans " + config.getRequestCooldown() + " secondes.");
                                    chatManager.sendHover(target, player);
                                    //player.sendMessage("Vous devez attendre " + TimeLeft + " secondes avant de pouvoir renvoyer une nouvelle demande.");
                                } else {
                                    player.sendMessage("Vous avez deja une demande en attente.");
                                }
                            } else player.sendMessage("Joueur deja en duel");
                        } else player.sendMessage("Vous etes deja en duel");
                    } else player.sendMessage("Vous ne pouvez pas défier vous meme en duel.");
                } else player.sendMessage("Ce joueur n'existe pas");
            }
            /*else {
                player.sendMessage("/duel <pseudo>");
                player.sendMessage("/duel deny <pseudo>");
                player.sendMessage("/duel accept <pseudo>");
            }*/
            else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("accept")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (this.request.hasRequest(target) && this.request.hasRequestFrom(target, player)) {
                            this.request.removeRequest(target,player,true);
                            target.sendMessage(target.getName() + " à accepté votre demande en duel.");
                            player.sendMessage("Vous avez accepté la demande en duel de " + player.getName());
                            arenaManager.addToArena(player, target);
                        } else player.sendMessage("Le joueur ne vous a pas envoyé de demande.");
                    } else player.sendMessage("Le joueur est hors-ligne.");

                } else if (args[0].equalsIgnoreCase("deny")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (!player.equals(target)) {
                            if (this.request.hasRequest(target) && this.request.hasRequestFrom(target, player)) {
                                this.request.removeRequest(target,player,false);
                                target.sendMessage(target.getName() + " à refusé votre demande en duel.");
                                player.sendMessage("Vous avez refusé la demande en duel de " + player.getName());
                            } else player.sendMessage("Le joueur ne vous a pas envoyé de demande.");
                        } else player.sendMessage("La cible ne peut pas etre vous même.");
                    } else player.sendMessage("Le joueur est hors-ligne.");
                } else if (args[0].equalsIgnoreCase("createarena")) {
                    if (player.hasPermission("duel.admin")) {
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
            else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("setspawn")) {
                    String arenaName = args[1];
                    if (arenaFile.getArenaconfig().get("arenas") != null) {
                        if (arenaFile.getArenaconfig().getConfigurationSection("arenas.").contains(arenaName)) {
                            if (args[2].equalsIgnoreCase("1")) {
                                arenaFile.setArenaLocation1(player.getLocation(), arenaName);
                                arenaFile.save();
                                player.sendMessage("Position 1 défini pour l'arène " + arenaName + " (" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + ").");
                            }
                            else if (args[2].equalsIgnoreCase("2")) {
                                arenaFile.setArenaLocation2(player.getLocation(), arenaName);
                                arenaFile.save();
                                player.sendMessage("Position 2 défini pour l'arène " + arenaName + " (" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + ").");
                            }//if both loc are set send message
                            if (arenaFile.getArenaconfig().get("arenas." + arenaName + ".1")!=null && arenaFile.getArenaconfig().get("arenas." + arenaName + ".2")!=null) {
                                player.sendMessage("L'arene est prete.");

                            }
                        }
                        else player.sendMessage("L'arène n'éxiste pas.");
                    }
                    else player.sendMessage("L'arène n'éxiste pas.");
                }
                else player.sendMessage("Commande inconnu.");
            }
            else player.sendMessage("Commande inconnu");
        }
        return true;

    }
}