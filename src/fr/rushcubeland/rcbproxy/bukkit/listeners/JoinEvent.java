package fr.rushcubeland.rcbproxy.bukkit.listeners;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbproxy.bukkit.RcbProxy;
import fr.rushcubeland.rcbproxy.bukkit.mod.ModModerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class JoinEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if(ModModerator.isInModData(player.getUniqueId().toString())){
            e.setJoinMessage(null);
            player.setAllowFlight(true);
            player.setFlying(true);
            ModModerator.giveTools(player.getUniqueId().toString());
            for(Player pls : Bukkit.getOnlinePlayers()){
                if(!ModModerator.isInModData(pls.getUniqueId().toString())){
                    pls.hidePlayer(RcbProxy.getInstance(), player);
                }
            }
        }
        else
        {
            for(Player pls : Bukkit.getOnlinePlayers()){
                if(ModModerator.isInModData(pls.getUniqueId().toString())){
                    player.hidePlayer(RcbProxy.getInstance(), pls);
                }
            }
        }
    }

}
