package fr.rushcubeland.rcbproxy.bungee.listeners;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.account.Account;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;

public class ProxiedPlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e){
        ProxiedPlayer player = e.getPlayer();
        Optional<Account> account = RcbProxy.getInstance().getAccount(player);
        if(account.isPresent()){
            account.get().onLogout();
        }

    }
}
