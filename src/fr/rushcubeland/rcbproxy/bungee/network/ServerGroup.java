package fr.rushcubeland.rcbproxy.bungee.network;

import java.util.ArrayList;

public enum ServerGroup {

    Lobby(),
    Minigame();

    private final ArrayList<ServerUnit> serversInServerGroup = new ArrayList<>();

    ServerGroup(){
    }

    public ArrayList<ServerUnit> getServersInGroup(){
        return serversInServerGroup;
    }

}
