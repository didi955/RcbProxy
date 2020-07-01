package fr.rushcubeland.rcbproxy.bukkit.listeners;

import fr.rushcubeland.rcbproxy.bukkit.mod.ModModerator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PickupEvent implements Listener {

    @EventHandler
    public void onPickup(EntityPickupItemEvent e){
        if(e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            if(ModModerator.isInModData(player.getUniqueId().toString())){
                e.setCancelled(true);
            }
        }
    }
}
