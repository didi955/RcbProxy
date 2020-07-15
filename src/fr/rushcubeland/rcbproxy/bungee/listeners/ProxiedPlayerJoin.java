package fr.rushcubeland.rcbproxy.bungee.listeners;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.account.Account;
import fr.rushcubeland.rcbproxy.bungee.account.RankUnit;
import fr.rushcubeland.rcbproxy.bungee.friends.Friend;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ProxiedPlayerJoin implements Listener {

    @EventHandler
    public void onConnect(PostLoginEvent e){
        ProxiedPlayer player = e.getPlayer();

        Account account = new Account(player.getUniqueId());
        account.onLogin();

        Friend.joinNotifFriends(player);

        initRankPlayerPermissions(player, account.getDatarank().getRank());

    }

    @EventHandler
    public void onLogin(LoginEvent e){
        RcbProxy.getInstance().getBanManager().checkDuration(e.getConnection().getUniqueId());

        if(RcbProxy.getInstance().getBanManager().isBanned(e.getConnection().getUniqueId())) {
            e.setCancelled(true);
            e.setCancelReason(new TextComponent("§cVous avez été banni !\n \n §6Raison : §f" +
                    RcbProxy.getInstance().getBanManager().getReason(e.getConnection().getUniqueId()) + "\n \n §aTemps restant : §f" +
                    RcbProxy.getInstance().getBanManager().getTimeLeft(e.getConnection().getUniqueId())));
        }
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
