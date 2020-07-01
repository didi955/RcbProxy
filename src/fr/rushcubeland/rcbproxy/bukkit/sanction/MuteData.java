package fr.rushcubeland.rcbproxy.bukkit.sanction;

import java.util.ArrayList;

public class MuteData {

    private static ArrayList<String> playerMute = new ArrayList<>();

    public static void removeMute(String targetUUID){
        if(playerMute.contains(targetUUID)){
            playerMute.remove(targetUUID);
        }
    }

    public static void addMute(String targetUUID){
        if(!playerMute.contains(targetUUID)){
            playerMute.add(targetUUID);
        }
    }

    public static boolean isInMuteData(String targetUUID){
        if(playerMute.contains(targetUUID)){
            return true;
        }
        return false;
    }

    public static ArrayList<String> getPlayerMute() {
        return playerMute;
    }
}
