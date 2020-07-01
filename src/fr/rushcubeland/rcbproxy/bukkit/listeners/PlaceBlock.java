package fr.rushcubeland.rcbproxy.bukkit.listeners;

import fr.rushcubeland.rcbproxy.bukkit.mod.ModModerator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlock implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player player = e.getPlayer();
        if(ModModerator.isInModData(player.getUniqueId().toString())){
            e.setCancelled(true);
        }
    }
}
