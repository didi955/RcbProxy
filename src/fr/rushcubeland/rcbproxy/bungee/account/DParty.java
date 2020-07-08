package fr.rushcubeland.rcbproxy.bungee.account;

import fr.rushcubeland.rcbproxy.bungee.parties.Party;

import java.util.UUID;

public class DParty extends AbstractData {

    private Party party;

    public DParty(UUID uuid) {
        this.uuid = uuid;
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
}
