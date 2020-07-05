package fr.rushcubeland.rcbproxy.bungee.network;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Random;

public class Network {

    public static void JoinLobby(ProxiedPlayer player){
        if(!player.getServer().getInfo().getName().startsWith("Lobby")){
            int r = new Random().nextInt(1);
            if(r == 0){
                player.connect(ServerUnit.Lobby_1.getServerInfo());
            }
        }
        else
        {
            player.sendMessage(new TextComponent("§cVous etes déjà connecté à un Lobby !"));
        }
    }
}
