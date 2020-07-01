package fr.rushcubeland.rcbproxy.bukkit.listeners;

import fr.rushcubeland.rcbproxy.bukkit.RcbProxy;
import fr.rushcubeland.rcbproxy.bukkit.mod.ModModerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(ModModerator.isInModData(e.getPlayer().getUniqueId().toString())){
            e.setJoinMessage(null);
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(true);
            ModModerator.giveTools(e.getPlayer().getUniqueId().toString());
            for(Player pls : Bukkit.getOnlinePlayers()){
                pls.hidePlayer(RcbProxy.getInstance(), e.getPlayer());
            }
        }
        else
        {
            for(Player pls : Bukkit.getOnlinePlayers()){
                if(ModModerator.isInModData(pls.getUniqueId().toString())){
                    e.getPlayer().hidePlayer(RcbProxy.getInstance(), pls);
                }
            }
        }
    }

}
