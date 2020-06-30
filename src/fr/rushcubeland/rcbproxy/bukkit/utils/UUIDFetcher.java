package fr.rushcubeland.rcbproxy.bukkit.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class UUIDFetcher {


    private static String insertDashUUID(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(8, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(13, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(18, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(23, "-");

        return sb.toString();
    }

    private static String deleteDashUUID(String uuid){
        return uuid.replaceAll("-", "");
    }

    public static String getUUIDFromName(String name){
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
            if(jsonObject == null){
                return null;
            }
            return insertDashUUID(jsonObject.get("id").getAsString());

        } catch (Exception ignored){
        }
        return null;
    }

    public static String getNameFromUUID(UUID uuid){
        String uuidf = deleteDashUUID(uuid.toString());
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidf
                    + "?unsigned=false");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
            if(jsonObject == null){
                return null;
            }
            return jsonObject.get("name").getAsString();

        } catch (Exception ignored){
        }
        return null;
    }
}
