package fr.rushcubeland.rcbproxy.bungee.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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

        String uuid = null;

        try {

            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();

            if(jsonObject != null){
                uuid = insertDashUUID(jsonObject.get("id").getAsString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return uuid;
    }

    public static String getNameFromUUID(UUID uuid){
        String uuidf = deleteDashUUID(uuid.toString());
        String name = null;

        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidf
                    + "?unsigned=false");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
            if(jsonObject != null){
                name = jsonObject.get("name").getAsString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }
}
