package fr.rushcubeland.commons;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.parties.Party;

import java.util.UUID;

public class AParty {

    private Party party;

    private final UUID uuid;
    private int maxPlayers;

    public AParty(UUID uuid, int maxPlayers) {
        this.uuid = uuid;
        this.maxPlayers = maxPlayers;
        if(!RcbProxy.getInstance().getAPartyList().contains(this)){
            RcbProxy.getInstance().getAPartyList().add(this);
        }
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public boolean isInParty(){
        return party != null;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public UUID getUuid() {
        return uuid;
    }
}
