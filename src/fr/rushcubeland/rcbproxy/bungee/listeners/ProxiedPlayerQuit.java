package fr.rushcubeland.rcbproxy.bungee.listeners;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.account.Account;
import fr.rushcubeland.rcbproxy.bungee.friends.Friend;
import fr.rushcubeland.rcbproxy.bungee.parties.Parties;
import fr.rushcubeland.rcbproxy.bungee.parties.Party;
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
        removePartyData(player);
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

    private void removePartyData(ProxiedPlayer player){
        Parties.getDatarequest().remove(player);
        Optional<Account> account = RcbProxy.getInstance().getAccount(player);
        if(account.isPresent()){
            if(account.get().getDataParty().isInParty()){
                Party party = account.get().getDataParty().getParty();
                party.removePlayer(player);
                if(!party.getPlayers().isEmpty()){
                    party.setCaptain(party.getPlayers().get(0));
                }
                account.get().getDataParty().setParty(null);
            }
        }
    }

}
