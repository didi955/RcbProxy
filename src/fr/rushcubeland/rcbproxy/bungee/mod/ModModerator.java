package fr.rushcubeland.rcbproxy.bungee.mod;

import java.util.ArrayList;

public class ModModerator {

    private static ArrayList<String> playersMod = new ArrayList<>();

    public static void removeMod(String targetUUID){
        if(playersMod.contains(targetUUID)){
            playersMod.remove(targetUUID);
        }
    }

    public static void addMod(String targetUUID){
        if(!playersMod.contains(targetUUID)){
            playersMod.add(targetUUID);
        }
    }

    public static boolean isInModData(String targetUUID){
        if(playersMod.contains(targetUUID)){
            return true;
        }
        return false;
    }

    public static ArrayList<String> getPlayersMod() {
        return playersMod;
    }
}