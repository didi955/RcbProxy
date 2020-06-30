package fr.rushcubeland.rcbproxy.bungee.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import org.bukkit.event.EventHandler;

public class Chat implements Listener {

    @EventHandler
    public void onChat(ChatEvent e){
        if(e.getSender() instanceof ProxiedPlayer){
            e.setCancelled(true);
        }
    }
}
