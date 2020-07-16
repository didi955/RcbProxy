package fr.rushcubeland.rcbproxy.bukkit.listeners;

import fr.rushcubeland.rcbproxy.bukkit.options.Options;
import fr.rushcubeland.rcbproxy.bukkit.sanction.MuteData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e){
        if(MuteData.isInMuteData(e.getPlayer().getUniqueId().toString())){
            if(!e.getMessage().startsWith("/")){
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cVous avez été mute !");
                return;
            }
        }
        for(Player pls : Bukkit.getOnlinePlayers()){
            if(!Options.hasChatActivated(pls)){
                e.getRecipients().remove(pls);
            }
            else e.getRecipients().add(pls);
        }
    }
}
