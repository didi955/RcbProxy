package fr.rushcubeland.rcbproxy.bukkit.options;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Options {

    private static final HashMap<Player, String> dataStateChat = new HashMap<>();

    public static HashMap<Player, String> getDataStateChat() {
        return dataStateChat;
    }

    public static boolean hasChatActivated(Player player){
        if(dataStateChat.containsKey(player)){
            String option = dataStateChat.get(player);
            if(option.equalsIgnoreCase("NEVER")){
                return true;
            }
        }
        return false;
    }
}
