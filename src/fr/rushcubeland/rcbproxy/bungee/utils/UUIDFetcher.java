package fr.rushcubeland.rcbproxy.bungee.utils;

import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.URL;

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



    public static String getUUIFromName(String name){
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

            return insertDashUUID(uuid);

        } catch (Exception e){
            e.printStackTrace();
        }
        return name;
    }
}
