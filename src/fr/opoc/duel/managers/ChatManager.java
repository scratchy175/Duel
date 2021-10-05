package fr.opoc.duel.managers;

import fr.opoc.duel.DuelPlugin;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

public class ChatManager {

    private final DuelPlugin plugin;

    public ChatManager(DuelPlugin plugin){
        this.plugin = plugin;
    }

    public void sendHover(Player player, Player player2){
        TextComponent base = new TextComponent();
        base.setText("Cliquez pour");
        TextComponent accept = new TextComponent();
        accept.setText("§2§l[Accepter]");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/duel accept " + player2.getName()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Cliquez pour accepter").create()));
        TextComponent deny = new TextComponent();
        deny.setText("§4§l[Refuser]");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/duel deny " + player2.getName()));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Cliquez pour refuser").create()));
        base.addExtra(" ");
        base.addExtra(accept);
        base.addExtra(" ");
        base.addExtra(deny);
        player.spigot().sendMessage(base);
    }
}
