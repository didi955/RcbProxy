package fr.rushcubeland.rcbproxy.bungee.network;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Arrays;
import java.util.Optional;

public enum ServerUnit {

    Lobby_1("Lobby", ServerGroup.Lobby, 500, "127.0.0.1", 25566),
    DeterrentBorder_1("DeterrentBorder_1", ServerGroup.Minigame, 16, "127.0.0.1", 25567);
    //Minigame_2("Minigame_2", ServerGroup.Minigame,16, "127.0.0.1", 25567),
    //Minigame_3("Minigame_3", ServerGroup.Minigame,16, "127.0.0.1", 25568);

    private String name;
    private int maxPlayers;
    private ServerGroup serverGroup;
    private int port;
    private int slots;
    private String ip;
    private ServerInfo serverInfo;

    ServerUnit(String name, ServerGroup serverGroup, int maxPlayers, String ip, int port){
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.serverGroup = serverGroup;
        this.port = port;
        this.ip = ip;
        serverGroup.getServersInGroup().add(this);
        serverInfo = ProxyServer.getInstance().getServers().get(name);
    }

    public static Optional<ServerUnit> getByName(String name){
        return Arrays.stream(values()).filter(r -> r.getName().equalsIgnoreCase(name)).findFirst();
    }

    public static Optional<ServerUnit> getByPort(int port){
        return Arrays.stream(values()).filter(r -> r.getPort() == port).findFirst();
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayers(){
        return maxPlayers;
    }

    public ServerGroup getServerGroup(){
        return serverGroup;
    }

    public Integer getPort(){
        return port;
    }

    public void setSlots(Integer playersCount){
        slots = playersCount;
    }

    public Integer getSlots(){
        return slots;
    }

    public String getIp() {
        return ip;
    }


}
