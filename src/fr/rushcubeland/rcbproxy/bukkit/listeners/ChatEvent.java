package fr.rushcubeland.rcbproxy.bukkit.listeners;

import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.rcbproxy.bukkit.RcbProxy;
import fr.rushcubeland.rcbproxy.bukkit.data.redis.RedisAccess;
import fr.rushcubeland.commons.options.OptionUnit;
import fr.rushcubeland.rcbproxy.bukkit.sanction.MuteData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

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
            RcbProxy.getInstance().getAccountOptionsCallback(pls, aOptions -> {
                if(!aOptions.getStateChat().equals(OptionUnit.OPEN)){
                    if(pls != e.getPlayer()){
                        e.getRecipients().remove(pls);
                    }
                }
                else
                {
                    e.getRecipients().add(pls);
                }
            });
        }
    }
}
