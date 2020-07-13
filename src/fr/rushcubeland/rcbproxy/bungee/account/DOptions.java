package fr.rushcubeland.rcbproxy.bungee.account;

import fr.rushcubeland.rcbproxy.bungee.options.OptionUnit;

import java.util.UUID;

public class DOptions extends AbstractData {

    private OptionUnit statePartyInvite;
    private OptionUnit stateFriendRequests;
    private OptionUnit stateChat;
    private OptionUnit stateFriendsStatutNotif;

    public DOptions(UUID uuid) {
        this.uuid = uuid;
    }

    public void setStatePartyInvite(OptionUnit statePartyInvit) {
        this.statePartyInvite = statePartyInvit;
    }

    public OptionUnit getStateChat() {
        return stateChat;
    }

    public void setStateChat(OptionUnit stateChat) {
        this.stateChat = stateChat;
    }

    public OptionUnit getStatePartyInvite() {
        return statePartyInvite;
    }

    public OptionUnit getStateFriendRequests() {
        return stateFriendRequests;
    }

    public void setStateFriendRequests(OptionUnit stateFriendRequests) {
        this.stateFriendRequests = stateFriendRequests;
    }

    public OptionUnit getStateFriendsStatutNotif() {
        return stateFriendsStatutNotif;
    }

    public void setStateFriendsStatutNotif(OptionUnit stateFriendsStatutNotif) {
        this.stateFriendsStatutNotif = stateFriendsStatutNotif;
    }
}
