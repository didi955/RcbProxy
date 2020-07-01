package fr.rushcubeland.rcbproxy.bukkit.listeners;

import fr.rushcubeland.rcbproxy.bukkit.sanction.MuteData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if(MuteData.isInMuteData(e.getPlayer().getUniqueId().toString())){
            if(!e.getMessage().startsWith("/")){
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cVous avez été mute !");
            }
        }
    }
}
