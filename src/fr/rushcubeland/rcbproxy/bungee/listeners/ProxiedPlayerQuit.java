package fr.rushcubeland.rcbproxy.bungee.listeners;

import fr.rushcubeland.commons.AParty;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
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

    }

    private void removeMpData(ProxiedPlayer player){
        ProxiedPlayer target = RcbProxy.getInstance().getMpData().get(player);
        RcbProxy.getInstance().getMpData().remove(target);
        RcbProxy.getInstance().getMpData().remove(player);
    }

    private void removePartyData(ProxiedPlayer player){
        Parties.getDatarequest().remove(player);
        Optional<AParty> aParty = RcbProxy.getInstance().getAccountParty(player);
        if(aParty.isPresent()){
            if(aParty.get().isInParty()){
                Party party = aParty.get().getParty();
                party.removePlayer(player);
                if(!party.getPlayers().isEmpty()){
                    party.setCaptain(party.getPlayers().get(0));
                }
                aParty.get().setParty(null);
            }
        }
    }
}
