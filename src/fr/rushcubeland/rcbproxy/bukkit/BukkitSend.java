package fr.rushcubeland.rcbproxy.bukkit;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.rushcubeland.rcbproxy.bukkit.utils.UUIDFetcher;
import org.bukkit.entity.Player;

public class BukkitSend {

    public static void banToProxy(Player player, String targetname, long durationSeconds, String reason){
        try {
            String uuid = UUIDFetcher.getUUIDFromName(targetname);
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Ban");
            out.writeUTF(uuid);
            out.writeLong(durationSeconds);
            out.writeUTF(reason);
            player.sendPluginMessage(RcbProxy.getInstance(), "rcbproxy:main", out.toByteArray());
        }
        catch (NullPointerException nullPointerException){
            nullPointerException.getStackTrace();
        }
    }

    public static void muteToProxy(Player player, String targetname, long durationSeconds, String reason){
        try {
            String uuid = UUIDFetcher.getUUIDFromName(targetname);
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Mute");
            out.writeUTF(uuid);
            out.writeLong(durationSeconds);
            out.writeUTF(reason);
            player.sendPluginMessage(RcbProxy.getInstance(), "rcbproxy:main", out.toByteArray());
        }
        catch (NullPointerException nullPointerException){
            nullPointerException.getStackTrace();
        }
    }

    public static void kickToProxy(Player player, String targetname, String reason){
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Kick");
            out.writeUTF(targetname);
            out.writeUTF(reason);
            player.sendPluginMessage(RcbProxy.getInstance(), "rcbproxy:main", out.toByteArray());
        }
        catch (NullPointerException nullPointerException){
            nullPointerException.getStackTrace();
        }
    }

}
