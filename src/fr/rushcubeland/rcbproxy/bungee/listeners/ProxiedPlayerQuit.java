package fr.rushcubeland.rcbproxy.bungee.listeners;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.account.Account;
import fr.rushcubeland.rcbproxy.bungee.friends.Friend;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;

public class ProxiedPlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e){
        ProxiedPlayer player = e.getPlayer();
        removeMpData(player);
        Friend.quitNotifFriends(player);
        Friend.onQuit(player);
        Optional<Account> account = RcbProxy.getInstance().getAccount(player);
        account.ifPresent(Account::onLogout);

    }

    private void removeMpData(ProxiedPlayer player){
        ProxiedPlayer target = RcbProxy.getInstance().getMpData().get(player);
        RcbProxy.getInstance().getMpData().remove(target);
        RcbProxy.getInstance().getMpData().remove(player);
    }

}
