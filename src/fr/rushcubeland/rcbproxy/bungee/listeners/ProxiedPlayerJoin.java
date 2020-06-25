package fr.rushcubeland.rcbproxy.bungee.listeners;

import fr.rushcubeland.rcbproxy.bungee.account.Account;
import fr.rushcubeland.rcbproxy.bungee.account.RankUnit;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxiedPlayerJoin implements Listener {

    @EventHandler
    public void onConnect(PostLoginEvent e){
        ProxiedPlayer player = e.getPlayer();

        Account account = new Account(player.getUniqueId());
        account.onLogin();

        initRankPlayerPermissions(player, account.getDatarank().getRank());

    }

    private void initRankPlayerPermissions(ProxiedPlayer player, RankUnit rank){
        if(rank.getPermissions().isEmpty()){
            return;
        }
        for(String perm : rank.getPermissions()){
            player.setPermission(perm, true);
        }
    }
}
