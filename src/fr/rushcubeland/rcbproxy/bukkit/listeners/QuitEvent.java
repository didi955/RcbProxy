package fr.rushcubeland.rcbproxy.bukkit.listeners;

import fr.rushcubeland.rcbproxy.bukkit.mod.ModModerator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if(ModModerator.isInModData(player.getUniqueId().toString())){
            e.setQuitMessage(null);
        }
    }
}
